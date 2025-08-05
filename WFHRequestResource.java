package resources;

import domain.WFHRequest;
import services.WFHRequestDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Date;

@Path("/wfhrequest")
public class WFHRequestResource {

    @POST
    @Path("/createrequest")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createWFHRequest(WFHRequest wfhRequest) throws SQLException {

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

        // 2. Check if a WFH request already exists for the user on the given date
        boolean isRequestExist = WFHRequestDAO.checkWFHRequestExists(userId, new java.sql.Date(wfhDate.getTime()));

        if (isRequestExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A WFH request already exists for this date!")
                    .build();
        }

        // 3. Create a new WFH request
        boolean isInserted = WFHRequestDAO.addWFHRequest(userId, new java.sql.Date(wfhDate.getTime()), wfhReason);

        if (isInserted) {
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
