package edu.grenoble.em.bourji;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.mturk.MTurkClient;

import java.net.URI;

/**
 * Created by Moe on 6/20/19.
 */
public class MTurkClientProvider {

    private final AwsConfig config;

    public MTurkClientProvider(AwsConfig config) {
        this.config = config;
    }

    public MTurkClient getClient() {
        AwsBasicCredentials cred = AwsBasicCredentials.create(config.getAccessId(), config.getSecretKey());
        StaticCredentialsProvider provider = StaticCredentialsProvider.create(cred);
        return MTurkClient.builder()
                .region(Region.US_EAST_1)
                .endpointOverride(URI.create(config.getEndpoint()))
                .credentialsProvider(provider)
                .build();
    }
}
