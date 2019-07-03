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
    private final String teacherQualificationId;
    private final String supervisorQualificationId;

    @JsonCreator
    public AwsConfig(
            @JsonProperty(value = "accessId", required = true) String accessId,
            @JsonProperty(value = "secretKey", required = true) String secretKey,
            @JsonProperty(value = "endpoint", required = true) String endpoint,
            @JsonProperty(value = "teacherQualificationId", required = true) String teacherQualificationId,
            @JsonProperty(value = "supervisorQualificationId", required = true) String supervisorQualificationId) {
        this.accessId = accessId;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
        this.teacherQualificationId = teacherQualificationId;
        this.supervisorQualificationId = supervisorQualificationId;
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

    public String getTeacherQualificationId() {
        return teacherQualificationId;
    }

    public String getSupervisorQualificationId() {
        return supervisorQualificationId;
    }
}
