package resources;

import domain.User;
import org.jboss.logging.MDC;
import services.UserService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

import utils.JWTUtility;

import org.mindrot.jbcrypt.BCrypt;

@Path("/home")
public class LoginResource {

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(User user) throws SQLException {
        String name = user.getUserName();
        String email = user.getUserEmail();
        String password = user.getUserPassword();

        if (name == null || name.isBlank() || password == null || password.isBlank() || email == null || email.isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Name, password, or email cannot be left empty!")
                    .build();
        }

        boolean emailExists = UserService.CheckIfEmailAlreadyUsed(email);
        if (emailExists) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A user with this email already exists!")
                    .build();
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        boolean inserted = UserService.SignUp(name, email, hashedPassword);

        if (inserted) {
            return Response.status(Response.Status.CREATED)
                    .entity("User created successfully.")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create user.")
                    .build();
        }
    }

    @Path("/login")
        @POST
        @Produces(MediaType.APPLICATION_JSON)
        @Consumes(MediaType.APPLICATION_JSON)
        public static Response login(User loginRequest) throws SQLException {
            String email = loginRequest.getUserEmail();
            String password = loginRequest.getUserPassword();
            // logger.info("Login attempt for user: {}", username);
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
               // logger.error("Login failed: Username or password is missing.");
                return Response.status(Response.Status.BAD_REQUEST).entity("Username or password is missing!").build();
            }
             User user = UserService.validateLogin(email, password);

            if (user != null) {
                int userID = UserService.getUserIDFromDatabase(email);
                user.setUserID(userID);
                String token = JWTUtility.generateToken(userID);
              //  user.setPassword("*********");

              //  String sessionID = UUID.randomUUID().toString();
               // MDC.put("userID", customer.getId());
                //MDC.put("sessionID", sessionID);
                //logger.info("Login successful for user: {}", username);

              //  MDC.clear();
                return Response.ok()
                        .header("Authorization", "Bearer " + token)
                        .entity(user + "\nLogin Successful!")
                        .build();
            } else {
              //  logger.error("Login failed for user: {} - Invalid credentials.");
                MDC.clear();
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid email or password.")
                        .build();
            }
        }

}
