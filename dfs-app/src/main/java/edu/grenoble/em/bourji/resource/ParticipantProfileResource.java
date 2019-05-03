package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.ExperimentMode;
import edu.grenoble.em.bourji.ParticipantProfiles;
import edu.grenoble.em.bourji.db.dao.InviteDAO;
import edu.grenoble.em.bourji.db.dao.ParticipantProfileDAO;
import edu.grenoble.em.bourji.db.pojo.ParticipantProfile;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Random;

import static edu.grenoble.em.bourji.ParticipantProfiles.*;

/**
 * Created by Moe on 4/18/18.
 */
@Path("/participant-profile")
@Produces(MediaType.APPLICATION_JSON)
@Authenticate
public class ParticipantProfileResource {

    private ParticipantProfileDAO dao;
    private InviteDAO inviteDAO;

    public ParticipantProfileResource(ParticipantProfileDAO dao, InviteDAO inviteDAO) {
        this.dao = dao;
        this.inviteDAO = inviteDAO;
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
                ParticipantProfiles newProfile = assignProfile();
                dao.add(user, newProfile);
                edu.grenoble.em.bourji.api.ParticipantProfile participantProfile = newProfile.getProfile();
                if (newProfile != NFS) {
                    participantProfile.setInvitationSent(false); // first sign-in
                    participantProfile.setInvitationPending(false);
                }
                return Response.ok(participantProfile).build();
            } else {
                edu.grenoble.em.bourji.api.ParticipantProfile participantProfile = ParticipantProfiles.valueOf(profile.getProfile()).getProfile();
                if (participantProfile.getMode() != ExperimentMode.NFS) {
                    participantProfile.setInvitationSent(inviteDAO.userIsInvited(user));
                    participantProfile.setInvitationPending(inviteDAO.isInvitationPending(user));
                }
                return Response.ok(participantProfile).build();
            }
        } catch(Throwable e) {
            return Respond.respondWithError(e.getMessage());
        }
    }


    private ParticipantProfiles assignProfile() {
        Random random = new Random();
        int randomNumber = random.nextInt(3) + 1;
        switch (randomNumber) {
            case 1:
                return NFS;
            case 2:
                return DFS;
            case 3:
                return IFS;
            default:
                throw new RuntimeException("Can not assign profile");
        }
    }
}