package resources;

import common.AuthHeader;
import domain.LunchSchedule;
import services.LunchScheduleService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Date;

import java.util.List;

import static utils.JWTUtility.validateToken;

@Path("/lunch")
public class LunchScheduleResource {

    @POST
    @Path("/addlunch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLunch(LunchSchedule lunchSchedule, @HeaderParam("Authorization") String authHeader) throws SQLException {

        Response response = null;

        response = LunchScheduleService.validateRole(lunchSchedule);
        if (response != null) {
            return response;
        }

        response = AuthHeader.validateHeader(authHeader);
        if (response != null) {
            return response;
        }

        String token = authHeader.substring(7);
        int AdderID = lunchSchedule.getAdderID();
        if (!validateToken(token,AdderID)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token.")
                    .build();
        }

        response = LunchScheduleService.validateLunch(lunchSchedule);
        if (response != null) {
            return response;
        }

        response = LunchScheduleService.checkLunchScheduleAvailability(lunchSchedule);
        if (response != null) {
            return response;
        }

        response = LunchScheduleService.addLunchSchedule(lunchSchedule);
        return response;
    }


    @POST
    @Path("/deletelunch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLunch(LunchSchedule lunchSchedule, @HeaderParam("Authorization") String authHeader) throws SQLException {

        Response response = null;

        response = LunchScheduleService.validateDate(lunchSchedule);
        if (response != null) {
            return response;
        }

        response = AuthHeader.validateHeader(authHeader);
        if (response != null) {
            return response;
        }

        String token = authHeader.substring(7);
        int AdderID = lunchSchedule.getAdderID();
        if (!validateToken(token,AdderID)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired token.")
                    .build();
        }

        response = LunchScheduleService.validateRole(lunchSchedule);
        if (response != null) {
            return response;
        }

        response = LunchScheduleService.checkLunchScheduleAvailability(lunchSchedule);

        if (response == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No lunch schedule exists for the given date!")
                    .build();
        }

        response = LunchScheduleService.deleteLunchSchedule(lunchSchedule);
        return response;
    }

    @GET
    @Path("/viewlunchcurrmonth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewLunchScheduleForThisMonth(@HeaderParam("Authorization") String authHeader) {

        Response response = AuthHeader.validateHeader(authHeader);
        if (response != null) {
            return response;
        }

        try {
            List<LunchSchedule> lunchSchedules = LunchScheduleService.viewLunchScheduleForThisMonth();

            if (lunchSchedules.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No lunch schedules found for this month.")
                        .build();
            }

            return Response.status(Response.Status.OK)
                    .entity(lunchSchedules)
                    .build();

        } catch (SQLException e) {

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching lunch schedules for this month.")
                    .build();
        }
    }

}
