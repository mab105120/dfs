package edu.grenoble.em.bourji.db.dao;

import edu.grenoble.em.bourji.db.pojo.Invite;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Moe on 4/9/19.
 */
public class InviteDAO extends AbstractDAO<Invite> {
    public InviteDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void addInvite(Invite invite) {
        persist(invite);
    }

    public List getPendingUserEmails() {
        return criteria().add(Restrictions.ne("stage", "COMPLETE"))
                .add(Restrictions.ne("stage", "REMINDER_SENT_4")).list();
    }

    public void updateInviteeStatus(Invite invite, String newStatus) {
        invite.setStage(newStatus);
        invite.setTime(Timestamp.valueOf(LocalDateTime.now()));
        persist(invite);
    }

    public Invite getInvitee(String id) {
        return get(id);
    }

    public boolean userIsInvited(String user) {
        Invite invite = getInvitee(user);
        return invite != null && !invite.getStage().equals("PENDING_INVITE");
    }

    public boolean isInvitationPending(String user) {
        Invite invite = getInvitee(user);
        return invite != null && invite.getStage().equals("PENDING_INVITE");
    }
}
