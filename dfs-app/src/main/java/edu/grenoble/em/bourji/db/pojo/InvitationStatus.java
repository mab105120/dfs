package edu.grenoble.em.bourji.db.pojo;

/**
 * Created by Moe on 5/11/19.
 */
public class InvitationStatus {

    private boolean invitationPending;
    private boolean invitationSent;

    public InvitationStatus() {
        // default no-arg constructor for jackson
    }

    public InvitationStatus(boolean invitationPending, boolean invitationSent) {
        this.invitationPending = invitationPending;
        this.invitationSent = invitationSent;
    }

    public boolean isInvitationPending() {
        return invitationPending;
    }

    public boolean isInvitationSent() {
        return invitationSent;
    }
}
