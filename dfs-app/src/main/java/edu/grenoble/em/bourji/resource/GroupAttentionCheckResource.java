package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.GroupAttentionCheckDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
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

    public GroupAttentionCheckResource(GroupAttentionCheckDAO groupAttentionCheckDAO, StatusDAO statusDAO) {
        this.groupAttentionCheckDAO = groupAttentionCheckDAO;
        this.statusDAO = statusDAO;
    }

    @POST
    @UnitOfWork
    public Response addGroupAttentionCheck(GroupAttentionCheck groupAttentionCheck,
                                           @Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        try {
            LOGGER.info("Saving attention check for " + user);
            groupAttentionCheck.setId(user);
            groupAttentionCheckDAO.add(groupAttentionCheck);
            statusDAO.add(new Status(user, ProgressStatus.ATT_CHECK_COMPLETE.name(), 0));
            return Response.ok().build();
        } catch (Throwable e) {
            String msg = String.format("Could not record attention check for %s. Error: %s",
                    user, e.getMessage());
            LOGGER.error(msg);
            return Respond.respondWithError(msg);
        }
    }

    @GET
    @Path("/is-complete")
    @UnitOfWork
    public Response groupAttentionCheckComplete(@Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        try {
            LOGGER.info(String.format("Checking on whether gorup attention check is complete for %s", user));
            boolean attCheckComplete = statusDAO.stepCompleted(user, ProgressStatus.ATT_CHECK_COMPLETE);
            return Response.ok(attCheckComplete).build();
        } catch (Throwable e) {
            String msg = String.format("Could not retrieve group attention check status for %s", user);
            LOGGER.info(msg);
            return Respond.respondWithError(msg);
        }
    }

}
