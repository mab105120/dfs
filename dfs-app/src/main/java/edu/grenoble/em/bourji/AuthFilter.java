package edu.grenoble.em.bourji;

import com.auth0.jwk.JwkException;
import edu.grenoble.em.bourji.api.BadResponse;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Moe on 11/24/17.
 */
@Authenticate
class AuthFilter implements ContainerRequestFilter {

    private final JwtTokenHelper helper;

    AuthFilter(JwtTokenHelper helper) {
        this.helper = helper;
    }

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String accessId = containerRequestContext.getHeaderString("Authorization");
        try {

            if (accessId == null || accessId.isEmpty()) {
                containerRequestContext.abortWith(Response
                        .status(HttpStatus.FORBIDDEN_403)
                        .entity(new BadResponse().withMessage("Missing authorization header"))
                        .build());
                return;
            }

            if(!accessId.startsWith("Bearer")) {
                containerRequestContext.abortWith(Response
                        .status(HttpStatus.FORBIDDEN_403)
                        .entity(new BadResponse().withMessage("Authorization header missing Bearer"))
                        .build());
                return;
            }

            accessId = accessId.substring(7);
            containerRequestContext.setProperty("user", helper.getUserIdFromToken(accessId));
        } catch (JwkException e) {
            containerRequestContext.abortWith(Response
                    .status(HttpStatus.FORBIDDEN_403)
                    .entity(new BadResponse().withMessage("Authorization error: " + e.getMessage()))
                    .build());
        }
    }
}
