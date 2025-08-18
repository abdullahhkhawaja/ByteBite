package resources;

import domain.WFHRequest;
import services.SQSService;
import services.WFHRequestService;
import common.AuthHeader;
import services.UserService;

import services.SlackNotificationService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Date;

import static common.AuthHeader.validateHeader;
import static utils.JWTUtility.validateToken;

@Path("/wfhrequest")
public class WFHRequestResource {

    @POST
    @Path("/createrequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)

    public Response createWFHRequest(WFHRequest wfhRequest, @HeaderParam("Authorization") String authHeader) throws SQLException {

        Response response = null;
        response = validateHeader(authHeader);
        if (response != null)
            return response;

        String token = authHeader.substring(7);
        int userID = wfhRequest.getUserID();
        if (!validateToken(token,userID)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token.")
                    .build();
        }

        int userId = wfhRequest.getUserID();
        Date wfhDate = wfhRequest.getWfhDate();
        String wfhReason = wfhRequest.getWfhReason();

        if (userId <= 0 || wfhDate == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("User ID, WFH date cannot be empty!")
                    .build();
        }
        Date today = new Date();
        if (wfhDate.before(today)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("WFH date cannot be in the past!")
                    .build();
        }

        boolean isRequestExist = WFHRequestService.checkWFHRequestExists(userId, new java.sql.Date(wfhDate.getTime()));

        if (isRequestExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A WFH request already exists for this date!")
                    .build();
        }
        boolean isInserted = WFHRequestService.addWFHRequest(userId, new java.sql.Date(wfhDate.getTime()), wfhReason);


        if (isInserted) {
            try {

                String userName = UserService.getUserNameFromID(userID);
                String message = userName + " will be working from home on " + wfhDate;
                SQSService sqsService = new SQSService();

                sqsService.sendMessageToSQS(message);
                sqsService.receiveMessageFromSQS();

            } catch (Exception e) {
                System.out.println("Failed to send Slack notification: " + e.getMessage());
            }

                return Response.status(Response.Status.CREATED)
                    .entity("WFH request created successfully.")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create WFH request.")
                    .build();
        }
    }
}
