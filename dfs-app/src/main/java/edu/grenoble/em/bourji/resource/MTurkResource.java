package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.AwsConfig;
import edu.grenoble.em.bourji.MTurkClientProvider;
import edu.grenoble.em.bourji.TimeUtils;
import edu.grenoble.em.bourji.db.dao.InviteDAO;
import edu.grenoble.em.bourji.db.dao.ParticipantProfileDAO;
import edu.grenoble.em.bourji.db.pojo.Invite;
import edu.grenoble.em.bourji.db.pojo.ParticipantProfile;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import software.amazon.awssdk.services.mturk.MTurkClient;
import software.amazon.awssdk.services.mturk.model.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Moe on 6/23/19.
 */
@Path("/mturk")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MTurkResource {

    private final AwsConfig config;
    private final InviteDAO inviteDAO;
    private final ParticipantProfileDAO profileDAO;
    private final long lag;
    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(MTurkResource.class);

    public MTurkResource(AwsConfig config, InviteDAO inviteDAO, ParticipantProfileDAO profileDAO, long lag) {
        this.config = config;
        this.inviteDAO = inviteDAO;
        this.profileDAO = profileDAO;
        this.lag = lag;
    }

    @Path("/get-worker-ids/{hitId}")
    @GET
    public Response getListOfWorkerIds(@PathParam("hitId") String hitId) {
        List<String> workerIds = new ArrayList<>();
        try {
            LOGGER.info("Getting list of worker Ids for HIT " + hitId);
            MTurkClient client = new MTurkClientProvider(config).getClient();
            ListAssignmentsForHitRequest req = ListAssignmentsForHitRequest.builder()
                    .hitId(hitId)
                    .assignmentStatuses(AssignmentStatus.SUBMITTED)
                    .build();
            ListAssignmentsForHitResponse response = client.listAssignmentsForHIT(req);
            response.assignments().forEach(assignment -> workerIds.add(assignment.workerId()));
            LOGGER.info(String.format("Returning %s worker IDs for HIT %s", workerIds.size(), hitId));
            return Response.ok(workerIds).build();
        } catch (Exception e) {
            String message = String.format("Failed to retrieve list of worker Ids for HIT#: %s. Details: %s", hitId, e.getMessage());
            LOGGER.error(message);
            return Response.serverError().entity(message).build();
        }
    }

    @Path("/invite")
    @POST
    @UnitOfWork
    public Response invite(@QueryParam("force") Boolean force) {
        try {
            LOGGER.info("Received request to invite participants. Force: " + force);
            if (force == null) force = false;
            LOGGER.info("Getting invite list");
            List<Invite> inviteList = inviteDAO.getPendingUserEmails();
            List<Invite> firstTimeInvites = getFirstTimersList(inviteList, force);
            List<Invite> reminderInvites = getReminderList(inviteList);
            LOGGER.info(String.format("Found %s on the invite list: first timers = %s, reminders = %s",
                    inviteList.size(), firstTimeInvites.size(), reminderInvites.size()));
            if (!firstTimeInvites.isEmpty()) {
                LOGGER.info("Sending first time invites");
                List<String> workerIds = firstTimeInvites.stream().map(Invite::getId).collect(Collectors.toList());
                List<ParticipantProfile> participants = getQualificationIdByWorkerId(workerIds);
                LOGGER.info("Assigning qualification type to participants");
                assignQualificationToWorkers(participants);
                LOGGER.info("Notifying participants of new HIT");
                sendNotification(participants, false);
                LOGGER.info("Updating first-time participant stage in database");
                for (Invite invite : firstTimeInvites)
                    inviteDAO.updateInviteeStatus(invite, "INVITE_SENT");
            }
            if (!reminderInvites.isEmpty()) {
                LOGGER.info("Sending reminder invites");
                List<String> workerIds = reminderInvites.stream().map(Invite::getId).collect(Collectors.toList());
                List<ParticipantProfile> participants = getQualificationIdByWorkerId(workerIds);
                sendNotification(participants, true);
                for (Invite invite : reminderInvites) {
                    String nextStage;
                    if (invite.getStage().equals("INVITE_SENT"))
                        nextStage = "REMINDER_SENT_1";
                    else {
                        int lastReminder = Integer.parseInt(invite.getStage().substring(invite.getStage().length() - 1));
                        nextStage = String.format("REMINDER_SENT_%s", lastReminder + 1);
                    }
                    LOGGER.info("Updating reminder participant stage in database");
                    inviteDAO.updateInviteeStatus(invite, nextStage);
                }
            }
            return Response.ok().build();
        } catch (Exception e) {
            String message = "Failed to invite workers to next HIT. Error: " + e.getMessage();
            LOGGER.error(message);
            return Response.serverError().entity(message).build();
        }
    }

    private List<Invite> getFirstTimersList(List<Invite> inviteList, boolean force) {
        List<Invite> qualifiedInvites = new ArrayList<>();
        for (Invite invite : inviteList) {
            long minutesSinceLastSeen = TimeUtils.minutesSince(invite.getTime());
            if (invite.getStage().startsWith("PENDING_INVITE")) {
                if (force)
                    qualifiedInvites.add(invite);
                else if (minutesSinceLastSeen > lag)
                    qualifiedInvites.add(invite);
            }
        }
        return qualifiedInvites;
    }

    private List<Invite> getReminderList(List<Invite> inviteList) {
        List<Invite> qualifiedInvites = new ArrayList<>();
        for (Invite invite : inviteList) {
            if ((invite.getStage().startsWith("INVITE_SENT") || invite.getStage().startsWith("REMINDER")))
                qualifiedInvites.add(invite);
        }
        return qualifiedInvites;
    }

    private void assignQualificationToWorkers(List<ParticipantProfile> participants) {
        MTurkClient client = new MTurkClientProvider(config).getClient();
        for (ParticipantProfile participant : participants) {
            AssociateQualificationWithWorkerRequest req = AssociateQualificationWithWorkerRequest.builder()
                    .qualificationTypeId(config.getInviteQualId())
                    .workerId(participant.getId())
                    .integerValue(0)
                    .sendNotification(false)
                    .build();
            client.associateQualificationWithWorker(req);
        }
    }

    private List<ParticipantProfile> getQualificationIdByWorkerId(List<String> workerIds) {
        return profileDAO.getParticipantProfiles(workerIds);
    }

    private void sendNotification(List<ParticipantProfile> participants, boolean isReminder) {
        String inviteSubject = "You are invited to a follow up HIT (Pay: $2 | Time: 3 mins)";
        String reminderSubject = "A HIT is waiting for you! (Pay: $2 | Time: 3 mins)";
        String subject = isReminder ? "REMINDER: " + reminderSubject : inviteSubject;
        List<String> dfsWorkerIds = filterProfiles(participants, "DFS").stream().map(ParticipantProfile::getId).collect(Collectors.toList());
        List<String> ifsWorkerIds = filterProfiles(participants, "IFS").stream().map(ParticipantProfile::getId).collect(Collectors.toList());
        List<String> dfsTWorkerIds = filterProfiles(participants, "DFS_T").stream().map(ParticipantProfile::getId).collect(Collectors.toList());
        List<String> ifsTWorkerIds = filterProfiles(participants, "IFS_T").stream().map(ParticipantProfile::getId).collect(Collectors.toList());
        if (!dfsWorkerIds.isEmpty()) notifyWorkers(dfsWorkerIds, subject, false, true);
        if (!ifsWorkerIds.isEmpty()) notifyWorkers(ifsWorkerIds, subject, false, false);
        if(!dfsTWorkerIds.isEmpty()) notifyWorkers(dfsTWorkerIds, subject, true, true);
        if(!ifsTWorkerIds.isEmpty()) notifyWorkers(ifsTWorkerIds, subject, true, false);
    }

    private void notifyWorkers(List<String> ids, String subject, boolean training, boolean dfs) {
        MTurkClient client = new MTurkClientProvider(config).getClient();
        client.notifyWorkers(
                NotifyWorkersRequest.builder()
                        .subject(subject)
                        .messageText(dfs ? getDfsInviteMessage(training) : getIfsInviteMessage(training))
                        .workerIds(ids)
                        .build()
        );
    }

    private List<ParticipantProfile> filterProfiles(List<ParticipantProfile> profiles, String group) {
        return profiles.stream().filter(profile -> profile.getProfile().equals(group)).collect(Collectors.toList());
    }


    private String getDfsInviteMessage(boolean forTraining) {
        return String.format(getInviteMessage(), forTraining ? "" : "eligible for tenure promotion",
                "After reviewing your profile, along with other profiles, two teachers selected you to provide appraisal feedback on their performance.",
                "Kingston High School - Evaluation Invite", "Teacher", forTraining ? "Teacher Training" : "Tenure Promotion");
    }

    private String getIfsInviteMessage(boolean forTraining) {
        return String.format(getInviteMessage(), forTraining ? "" : "eligible for tenure promotion",
                "After reviewing your profile, along with other profiles, a teacher supervisor selected you to provide appraisal feedback on two of their teachers' performance.",
                "Kingston High School - Evaluation Invite", "Teacher Supervisor", forTraining ? "Teacher Training" : "Tenure Promotion");
    }

    private String getInviteMessage() {
        return "Hello,\nYou have participated in a HIT titled 'Kingston High School - Teacher Evaluation'. " +
                "In that HIT, you were asked to help evaluate job performance of teachers %s. " +
                "At the end of the HIT an anonymous profile was created " +
                "for you based on your experience with providing job reviews and your performance on the training rounds. %s\n" +
                "In your worker dashboard, please search for and complete HIT named '%s'. You have been given the proper qualifications to participate in that HIT. " +
                "The HIT takes 3 minutes and pays $2.\n\nSUMMARY:\nInviter: %s\nEvaluation Purpose: %s\n----\n" +
                "Thank you";
    }
}
