package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.*;
import edu.grenoble.em.bourji.api.InviteRequest;
import edu.grenoble.em.bourji.api.Progress;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.InviteDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.InvitationStatus;
import edu.grenoble.em.bourji.db.pojo.Invite;
import edu.grenoble.em.bourji.db.pojo.Status;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import software.amazon.awssdk.services.mturk.MTurkClient;
import software.amazon.awssdk.services.mturk.model.AssociateQualificationWithWorkerRequest;
import software.amazon.awssdk.services.mturk.model.AssociateQualificationWithWorkerResponse;

import javax.mail.MessagingException;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/23/2017.
 */
@Path("/status")
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class StatusResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(StatusResource.class);
    private StatusDAO statusDAO;
    private InviteDAO inviteDAO;
    private final AwsConfig awsConfig;
    private final EmailConfiguration emailConfig;

    public StatusResource(StatusDAO statusDAO, InviteDAO inviteDAO, AwsConfig config, EmailConfiguration emailConfig) {
        this.statusDAO = statusDAO;
        this.inviteDAO = inviteDAO;
        this.awsConfig = config;
        this.emailConfig = emailConfig;
    }

    @GET
    @Path("/progress")
    @UnitOfWork
    public Response getProgressStatus(@Context ContainerRequestContext requestContext) {
        try {
            String user = requestContext.getProperty("user").toString();
            LOGGER.info(String.format("Getting progress for user (%s)", user));
            return Response.ok(new Progress(statusDAO.getProgress(user))).build();
        } catch (Throwable e) {
            String message = "Unable to get progress. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/step-is-completed/{step}")
    @UnitOfWork
    public Response isQuestionnaireCompleted(@PathParam("step") String step,
                                             @Context ContainerRequestContext requestContext) {
        LOGGER.info("Checking if step (" + step + ") is completed");

        try {
            String userId = requestContext.getProperty("user").toString();
            boolean isCompleted = statusDAO.stepCompleted(userId, ProgressStatus.valueOf(step));
            LOGGER.info(String.format("step %s completed for %s is %s", step, userId, isCompleted));
            return Response.ok(isCompleted).build();
        } catch (Throwable e) {
            String message = "Failed to retrieve whether user completed step (" + step + "). Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @POST
    @Path("/request-invite")
    @UnitOfWork
    public Response requestInvite(InviteRequest req, @Context ContainerRequestContext requestContext) {
        String user = requestContext.getProperty("user").toString();
        LOGGER.info("Received invite request from " + user);
        try {
            if (inviteDAO.getInvitee(user) != null) {
                return Respond.respondWithError("This profile has already been added. No action required");
            }
            Invite invite = new Invite(user, "PENDING_INVITE", req.getEmail());
            inviteDAO.addInvite(invite);
            statusDAO.add(new Status(user, "CONSENT", 0));
            LOGGER.info(String.format("Assigning Stage 1 complete qualification to %s", user));
            assignEvalCompleteQual(user);
            return Response.ok().status(Response.Status.CREATED).build();
        } catch (Throwable e) {
            String message = String.format("Failed to record invite request for %s. Error: %s", user, e.getMessage());
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    private void assignEvalCompleteQual(String workerId) throws MessagingException {
        String qualId = awsConfig.getEvaluationCompleteQualId();
        MTurkClient client = new MTurkClientProvider(awsConfig).getClient();
        AssociateQualificationWithWorkerRequest req = AssociateQualificationWithWorkerRequest.builder()
                .qualificationTypeId(qualId)
                .workerId(workerId)
                .integerValue(0)
                .sendNotification(false)
                .build();
        AssociateQualificationWithWorkerResponse res = client.associateQualificationWithWorker(req);
        if (res.sdkHttpResponse().statusCode() != 200) {
            int statusCode = res.sdkHttpResponse().statusCode();
            String statusText = res.sdkHttpResponse().statusText().orElse("");
            new EmailService(emailConfig).sendEmail(
                    "mohd.bourji@gmail.com",
                    "Failed to assign Profile Complete qualification",
                    String.format("Failed to assign qual: %s to worker Id: %s. Status code: %s, status text: %s", qualId, workerId, statusCode, statusText)
            );
        }
    }

    @GET
    @Path("/invitation-status")
    @UnitOfWork
    public Response getInvitationStatus(@Context ContainerRequestContext req) {
        String user = req.getProperty("user").toString();
        try {
            boolean invitePending = inviteDAO.isInvitationPending(user);
            boolean inviteSent = inviteDAO.userIsInvited(user);
            InvitationStatus invitationStatus = new InvitationStatus(invitePending, inviteSent);
            return Response.ok(invitationStatus).build();
        } catch (Throwable e) {
            String msg = String.format("Failed to retrieve invitation status for %s. Error: %s", user, e.getMessage());
            LOGGER.error(msg);
            return Respond.respondWithError(msg);
        }
    }

    @GET
    @Path("/completed-profiles-count")
    @UnitOfWork
    public Response getCompletedProfilesCount(@QueryParam("profile") String groupProfile) {
        return Response.ok(statusDAO.numberOfCompletedProfile(groupProfile)).build();
    }

}