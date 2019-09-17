package edu.grenoble.em.bourji;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Moe on 6/20/19.
 */
public class AwsConfig {
    private final String accessId;
    private final String secretKey;
    private final String endpoint;
    private final String inviteQualId;
    private final String evaluationCompleteQualId;
    private final String inviteCompleteQualId;

    @JsonCreator
    public AwsConfig(
            @JsonProperty(value = "accessId", required = true) String accessId,
            @JsonProperty(value = "secretKey", required = true) String secretKey,
            @JsonProperty(value = "endpoint", required = true) String endpoint,
            @JsonProperty(value = "inviteQualId", required = true) String inviteQualId,
            @JsonProperty(value = "evaluationCompleteQualId", required = true) String evaluationCompleteQualId,
            @JsonProperty(value = "inviteCompleteQualId", required = true) String inviteCompleteQualId) {
        this.accessId = accessId;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
        this.inviteQualId = inviteQualId;
        this.evaluationCompleteQualId = evaluationCompleteQualId;
        this.inviteCompleteQualId = inviteCompleteQualId;
    }

    public String getAccessId() {
        return accessId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getInviteQualId() {
        return inviteQualId;
    }

    public String getInviteCompleteQualId() {
        return inviteCompleteQualId;
    }

    public String getEvaluationCompleteQualId() {
        return evaluationCompleteQualId;
    }
}
