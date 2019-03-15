package edu.grenoble.em.bourji;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.interfaces.RSAPublicKey;

/**
 * Created by Moe on 9/9/2017.
 */
class JwtTokenHelper {

    private final String authDomain;
    private final RSAPublicKey pk;

    JwtTokenHelper(String authDomain, String kid) throws JwkException {
        this.authDomain = authDomain;
        JwkProvider provider = new UrlJwkProvider(String.format("https://%s/", authDomain));
        Jwk jwk = provider.get(kid);
        this.pk = (RSAPublicKey) jwk.getPublicKey();
    }

    String getUserIdFromToken(String access_id) throws JwkException {
        try {
            Algorithm algorithm = Algorithm.RSA256(pk, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(String.format("https://%s/", authDomain))
                    .build();
            DecodedJWT decodedJWT = verifier.verify(access_id);
            return decodedJWT.getClaim("name").asString();
        } catch(Throwable e) {
            throw new JwkException(e.getMessage(), e);
        }
    }
}