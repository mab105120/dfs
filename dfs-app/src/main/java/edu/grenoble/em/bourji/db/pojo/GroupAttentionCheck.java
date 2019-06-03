package edu.grenoble.em.bourji.db.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Moe on 5/6/19.
 */
@Entity
@Table(name = "ATT_CHECK")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupAttentionCheck {

    @Id
    @Column(name = "ID", nullable = false, length = 64)
    private String id;
    @Column(name = "DAYS_PROFILE_COMPLETE", nullable = false)
    private String daysSinceProfileComplete;
    @Column(name = "INVITER", nullable = false, length = 100)
    private String inviter;
    @Column(name = "PURPOSE", nullable = false, length = 100)
    private String purpose;
    @Column(name = "INVITATION_REASON", nullable = false, length = 1000)
    private String invitationReasons;
    @Column(name = "REASON_EXPLANATION", nullable = false, length = 1000)
    private String reasonExplanation;
    @Column(name = "CONFIRMATION", nullable = false)
    private String confirmation;
    @Column(name = "PASS", nullable = false)
    private boolean pass;

    public GroupAttentionCheck() {
        // default no-arg constructor for hibernate
    }

    public String getId() {
        return id;
    }

    public String getDaysSinceProfileComplete() {
        return daysSinceProfileComplete;
    }

    public String getInviter() {
        return inviter;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInvitationReasons() {
        return invitationReasons;
    }

    public String getReasonExplanation() {
        return reasonExplanation;
    }

    public boolean isPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }
}
