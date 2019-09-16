package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.ParticipantProfiles;
import edu.grenoble.em.bourji.db.dao.ParticipantProfileDAO;
import edu.grenoble.em.bourji.db.dao.StatusDAO;
import edu.grenoble.em.bourji.db.pojo.ParticipantProfile;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 4/18/18.
 */
@Path("/participant-profile")
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class ParticipantProfileResource {

    private ParticipantProfileDAO dao;
    private StatusDAO statusDAO;

    public ParticipantProfileResource(ParticipantProfileDAO dao, StatusDAO statusDAO) {
        this.dao = dao;
        this.statusDAO = statusDAO;
    }

    /**
     * Assign a participant profile if one doesn't exist, return if profile exists
     *
     * @param requestContext http request context
     * @return Assign a participant profile if one doesn't exist, return if profile exists
     */

    @GET
    @UnitOfWork
    public Response getParticipantProfile(@Context ContainerRequestContext requestContext) {
        try {
            String user = requestContext.getProperty("user").toString();
            ParticipantProfile profile = dao.getParticipantProfile(user);
            if (profile == null) {
                boolean reachedTheLimit = reachedTheLimit();
                ParticipantProfiles newProfile = assignProfile(reachedTheLimit);
                dao.add(user, newProfile);
                return Response.ok(newProfile.getProfile()).build();
            } else
                return Response.ok(ParticipantProfiles.valueOf(profile.getProfile()).getProfile()).build();
        } catch(Throwable e) {
            return Respond.respondWithError(e.getMessage());
        }
    }

    private boolean reachedTheLimit() {
        return statusDAO.numberOfCompletedProfile("DFS") >= 60;
    }

    private ParticipantProfiles assignProfile(boolean excludeNfs) {
        return ParticipantProfiles.DFS;
//        Random random = new Random();
//        int randomNumber = random.nextInt(2) + 1;
//        switch (randomNumber) {
//            case 1:
//                return ParticipantProfiles.IFS;
//            case 2:
//                return ParticipantProfiles.DFS;
////            case 3:
////                return ParticipantProfiles.NFS;
//            default:
//                throw new RuntimeException("Can not assign profile");
//        }
    }
}