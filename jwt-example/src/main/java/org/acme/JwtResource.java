package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.security.PrivateKey;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Path("/jwt")
public class JwtResource {

    @Inject
    KeyLoader keyLoader;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPrivateKey() {
        try {
            PrivateKey privateKey = keyLoader.loadPrivateKey();
            return "Private Key Loaded Successfully: " + privateKey;
        } catch (Exception e) {
            return "Error loading private key: " + e.getMessage();
        }
    }

    @GET
    @Path("/token")
    @Produces(MediaType.TEXT_PLAIN)
    public String getToken() {
        try {
            PrivateKey privateKey = keyLoader.loadPrivateKey();
            String jwt = Jwts.builder()
                    .setSubject("user@example.com") // Defina o assunto do token
                    .signWith(SignatureAlgorithm.RS256, privateKey) // Assine o token com a chave privada
                    .compact();
            return "Generated JWT: " + jwt;
        } catch (Exception e) {
            return "Error generating JWT: " + e.getMessage();
        }
    }
}
