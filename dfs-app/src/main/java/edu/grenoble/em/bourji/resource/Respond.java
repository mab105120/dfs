package edu.grenoble.em.bourji.resource;

import edu.grenoble.em.bourji.api.BadResponse;

import javax.ws.rs.core.Response;

/**
 * Created by Moe on 9/18/17.
 */
class Respond {

    static Response respondWithError(String errorMessage) {
        BadResponse response = new BadResponse().withMessage(errorMessage);
        return Response
                .status(500)
                .entity(response)
                .build();
    }
}
