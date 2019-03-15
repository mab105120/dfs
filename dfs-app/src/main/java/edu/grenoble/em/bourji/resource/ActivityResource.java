package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.Authenticate;
import edu.grenoble.em.bourji.db.dao.ActivityDAO;
import edu.grenoble.em.bourji.db.pojo.Activity;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/24/17.
 */
@Path("/activity")
@Authenticate
public class ActivityResource {

    private final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ActivityResource.class);
    private final ActivityDAO dao;

    public ActivityResource(ActivityDAO dao) {
        this.dao = dao;
    }

    @POST
    @Path("/login")
    @UnitOfWork
    public Response recordLogin(@Context ContainerRequestContext requestContext) {
        try {
            String userId = requestContext.getProperty("user").toString();
            LOGGER.info(String.format("Recording user %s login", userId));
            dao.add(new Activity(userId, "IN"));
            return Response.ok(userId).build();
        } catch (Throwable e) {
            return Respond.respondWithError("Unable to record login. Error: " + e.getMessage());
        }
    }

    @POST
    @Path("/logout")
    @UnitOfWork
    public Response recordLogout(@Context ContainerRequestContext requestContext) {
        try {
            String userId = requestContext.getProperty("user").toString();
            LOGGER.info(String.format("Recording user %s logout", userId));
            dao.add(new Activity(userId, "OUT"));
            return Response.ok().build();
        } catch (Throwable e) {
            String errorMessage = String.format("Unable to record logout status. Reason: %s", e.getMessage());
            LOGGER.error(errorMessage);
            return Respond.respondWithError(errorMessage);
        }
    }
}