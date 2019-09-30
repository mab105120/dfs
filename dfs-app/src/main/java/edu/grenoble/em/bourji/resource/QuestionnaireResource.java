package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.api.ProgressStatus;
import edu.grenoble.em.bourji.db.dao.QuestionnaireDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.Status;
import edu.grenoble.em.bourji.db.pojo.UserDemographic;
import edu.grenoble.em.bourji.db.pojo.UserExperience;
import edu.grenoble.em.bourji.db.pojo.UserRecipOrientation;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Moe on 9/9/2017.
 */
@Path("/questionnaire")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class QuestionnaireResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(QuestionnaireResource.class);
    private final QuestionnaireDAO dao;
    private final StatusDAO statusDAO;

    public QuestionnaireResource(QuestionnaireDAO dao, StatusDAO statusDAO) {
        this.dao = dao;
        this.statusDAO = statusDAO;
    }

    @POST
    @Path("/user-demographic")
    @UnitOfWork
    public Response addUserDemographic(UserDemographic userDemographic,
                                       @Context ContainerRequestContext requestContext) {

        try {
            String userId = requestContext.getProperty("user").toString();
            int submissionId = dao.getUserDemographicDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting demographic questionnaire response for user %s submission id %s ", userId, submissionId));
            userDemographic.setUser(userId);
            userDemographic.setSubmissionId(submissionId);
            dao.getUserDemographicDAO().add(userDemographic);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_DEMO", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_DEMO.name(), submissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-experience")
    @UnitOfWork
    public Response addUserExperience(UserExperience userExperience,
                                      @Context ContainerRequestContext requestContext) {

        try {
            String userId = requestContext.getProperty("user").toString();
            int submissionId = dao.getUserExperienceDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting experience questionnaire response for user %s submission id %s ", userId, submissionId));
            userExperience.setUser(userId);
            userExperience.setSubmissionId(submissionId);
            dao.getUserExperienceDAO().add(userExperience);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_EXP", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_EXP.name(), submissionId));
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to save response. Error: " + e.getMessage());
        }
        return Response.ok().build();
    }

    @POST
    @Path("/user-ro")
    @UnitOfWork
    public Response addUserRecipOrientation(List<UserRecipOrientation> userRecipOrientation,
                                            @Context ContainerRequestContext requestContext) {
        String userId = requestContext.getProperty("user").toString();
        try {
            int submissionId = dao.getUserRecipOrientationDAO().getNextSubmissionId(userId);
            LOGGER.info(String.format("Posting RO questionnaire response for user %s submission id %s ", userId, submissionId));
            userRecipOrientation.stream().forEach(res -> {
                res.setUser(userId);
                res.setSubmissionId(submissionId);
            });
            dao.getUserRecipOrientationDAO().addAll(userRecipOrientation);
            LOGGER.info(String.format("Setting user (%s) status to QUEST_RO", userId));
            statusDAO.add(new Status(userId, ProgressStatus.QUEST_RO.name(), submissionId));
        } catch(Throwable e) {
            String msg = "Unable to store user recip orientation for user ID " + userId + ". details: " + e.getMessage();
            LOGGER.error(msg);
            return Respond.respondWithError(msg);
        }
        return Response.ok().build();
    }

    @GET
    @Path("/user-ro")
    @UnitOfWork
    public Response getUserReciprocationOrientation(@Context ContainerRequestContext requestContext) {
        LOGGER.info("Getting user reciprocation orientation scores to populate form");
        try {
            String userId = requestContext.getProperty("user").toString();
            List<UserRecipOrientation> userRecipOrientation = dao.getUserRecipOrientationDAO().getUserRecipOrientation(userId);
            LOGGER.info("Successfully retrieved reciprocation orientation scores for: " + userId);
            return Response.ok(userRecipOrientation).build();
        } catch (Throwable e) {
            String errorMsg = "Unable to retrieve user reciprocation orientation scores. Details: " + e.getMessage();
            LOGGER.error(errorMsg);
            return Respond.respondWithError(errorMsg);
        }
    }

    @GET
    @Path("/user-demographic")
    @UnitOfWork
    public Response getUserDemographic(@Context ContainerRequestContext requestContext) {
        LOGGER.info("Getting user demographic input to populate form");

        try {
            String userId = requestContext.getProperty("user").toString();
            UserDemographic userDemographic = dao.getUserDemographicDAO().getUserDemographics(userId);
            LOGGER.info("Retrieved user demographics for " + userId);
            return Response.ok(userDemographic).build();
        } catch (Throwable e) {
            String message = "Unable to get user demographics. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }

    @GET
    @Path("/user-experience")
    @UnitOfWork
    public Response getUserExperience(@Context ContainerRequestContext requestContext) {
        LOGGER.info("Getting user experience input to populate form");

        try {
            String userId = requestContext.getProperty("user").toString();
            UserExperience userExperience = dao.getUserExperienceDAO().getUserExperience(userId);
            LOGGER.info("Retrieved user experience details for " + userId);
            return Response.ok(userExperience).build();
        } catch (Throwable e) {
            String message = "Unable to get user demographics. Details: " + e.getMessage();
            LOGGER.error(message);
            return Respond.respondWithError(message);
        }
    }
}