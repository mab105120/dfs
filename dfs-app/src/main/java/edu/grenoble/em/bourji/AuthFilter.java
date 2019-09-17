package edu.grenoble.em.bourji;

import edu.grenoble.em.bourji.api.BadResponse;
import org.eclipse.jetty.http.HttpStatus;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Created by Moe on 11/24/17.
 */
@Authenticate
class AuthFilter implements ContainerRequestFilter {


    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String mid = containerRequestContext.getHeaderString("workerId");
        try {
            if (mid == null || mid.isEmpty()) {
                containerRequestContext.abortWith(Response
                        .status(HttpStatus.FORBIDDEN_403)
                        .type(MediaType.APPLICATION_JSON_TYPE)
                        .entity(new BadResponse().withMessage("Authorization error: Missing MID."))
                        .build());
            }
            containerRequestContext.setProperty("user", mid);
        } catch (Exception e) {
            containerRequestContext.abortWith(Response
                    .status(HttpStatus.FORBIDDEN_403)
                    .type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(new BadResponse().withMessage("Authorization error: " + e.getMessage()))
                    .build());
        }
    }
}
