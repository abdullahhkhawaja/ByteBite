package common;

import javax.ws.rs.core.Response;


public class AuthHeader {
    public static Response validateHeader(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Authorization header is missing or empty.").build();
        }
        if (!authHeader.startsWith("Bearer ") || authHeader.length() < 8) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid Authorization format. Expected 'Bearer <token>'").build();
        }
        String token = authHeader.substring(7);
        return null;
    }
}