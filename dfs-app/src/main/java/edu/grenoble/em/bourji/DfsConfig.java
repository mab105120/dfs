package edu.grenoble.em.bourji;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by Moe on 8/16/2017.
 */
public class DfsConfig extends Configuration {

    @Valid
    @NotEmpty
    @JsonProperty("authDomain")
    private String authDomain;

    @Valid
    @NotEmpty
    @JsonProperty("kid")
    private String kid;

    @JsonProperty("database")
    @NotNull
    private DataSourceFactory dataSourceFactory;

    @JsonProperty("email")
    @NotNull
    private EmailConfiguration emailConfiguration;

    @JsonProperty("invitation")
    private InviteConfig inviteConfig;

    @JsonProperty("awsConfig")
    private AwsConfig awsConfig;

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    public String getKid() {
        return this.kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getAuth0Domain() {
        return authDomain;
    }

    public void setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
    }

    public EmailConfiguration getEmailConfiguration() {
        return emailConfiguration;
    }

    public InviteConfig getInviteConfig() {
        return inviteConfig;
    }

    public AwsConfig getAwsConfig() {
        return awsConfig;
    }
}
