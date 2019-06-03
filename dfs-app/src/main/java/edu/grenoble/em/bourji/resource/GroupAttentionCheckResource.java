package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.GroupAttentionCheckDAO;
import edu.grenoble.em.bourji.db.dao.InviteDAO;
import edu.grenoble.em.bourji.db.dao.ParticipantProfileDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus;
import edu.grenoble.em.bourji.db.pojo.GroupAttentionCheck;
import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus.PASS;
import static edu.grenoble.em.bourji.db.pojo.AttentionCheckStatus.PENDING;

/**
 * Created by Moe on 5/6/19.
 */
@Path("/group-att-check")
@Consumes(MediaType.APPLICATION_JSON)
@Authenticate
public class GroupAttentionCheckResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(AbsoluteEvaluationResource.class);
    private GroupAttentionCheckDAO groupAttentionCheckDAO;
    private StatusDAO statusDAO;
    private ParticipantProfileDAO profileDAO;
    private InviteDAO inviteDAO;

    public GroupAttentionCheckResource(GroupAttentionCheckDAO groupAttentionCheckDAO, StatusDAO statusDAO,
                                       ParticipantProfileDAO profileDAO, InviteDAO inviteDAO) {
        this.groupAttentionCheckDAO = groupAttentionCheckDAO;
        this.statusDAO = statusDAO;
        this.profileDAO = profileDAO;
        this.inviteDAO = inviteDAO;
    }

    @POST
    @UnitOfWork
    public Response addGroupAttentionCheck(GroupAttentionCheck groupAttentionCheck,
                                           @Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        try {
            LOGGER.info("Saving attention check for " + user);
            String profile = profileDAO.getParticipantProfile(user).getProfile();
            AttentionCheckStatus status = groupAttentionCheckDAO.getStatus(user);
            if (status != PENDING) {
                return Response.ok(status == PASS).build();
            }
            boolean userPassed = userPassAttentionCheck(profile, groupAttentionCheck);
            groupAttentionCheck.setId(user);
            groupAttentionCheck.setPass(userPassed);
            groupAttentionCheckDAO.add(groupAttentionCheck);
            statusDAO.add(new Status(user, userPassed ? ProgressStatus.ATT_CHECK_COMPLETE.name() : ProgressStatus.ATT_CHECK_FAIL.name()
                    , 0));
            if (!userPassed)
                inviteDAO.updateInviteeStatus(inviteDAO.getInvitee(user), "VOID");
            return Response.ok(userPassed).build();
        } catch (Throwable e) {
            String msg = String.format("Could not record attention check for %s. Error: %s",
                    user, e.getMessage());
            LOGGER.error(msg);
            return Respond.respondWithError(msg);
        }
    }

    private boolean userPassAttentionCheck(String profile, GroupAttentionCheck groupAttentionCheck) {
        if (!groupAttentionCheck.getPurpose().equals("Tenure promotion") || groupAttentionCheck.getConfirmation().equalsIgnoreCase("No"))
            return false;
        if (profile.equals("DFS"))
            return groupAttentionCheck.getInviter().equalsIgnoreCase("A teacher");
        return !profile.equals("IFS") || groupAttentionCheck.getInviter().equalsIgnoreCase("A teacher's supervisor");
    }

    @GET
    @Path("/is-complete")
    @UnitOfWork
    public Response groupAttentionCheckComplete(@Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        try {
            LOGGER.info(String.format("Checking on whether group attention check is complete for %s", user));
            AttentionCheckStatus status = groupAttentionCheckDAO.getStatus(user);
            return Response.ok(status.name()).build();
        } catch (Throwable e) {
            String msg = String.format("Could not retrieve group attention check status for %s", user);
            LOGGER.info(msg);
            return Respond.respondWithError(msg);
        }
    }

}
