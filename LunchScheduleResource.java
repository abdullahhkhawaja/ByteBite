package resources;

import domain.LunchSchedule;
import services.LunchScheduleDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.Date;

import java.util.List;

@Path("/lunch")
public class LunchScheduleResource {

    @POST
    @Path("/addlunch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLunch(LunchSchedule lunchSchedule) throws SQLException {
        // Extract data from the request body
        Date date = lunchSchedule.getLunchDate();
        String menu = lunchSchedule.getLunchMenu();

        // 1. Validate inputs
        if (date == null || menu == null || menu.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date and menu cannot be empty!")
                    .build();
        }

        Date today = new Date();
        if (date.before(today)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be in the past!")
                    .build();
        }

        boolean lunchScheduleAvailability = LunchScheduleDAO.checkLunchScheduleAvailability(new java.sql.Date(date.getTime()));

        if (!lunchScheduleAvailability) {
            return Response.status(Response.Status.BAD_REQUEST).entity("A lunch schedule already exists for entry!").build();
        }

        boolean isInserted = LunchScheduleDAO.addLunchSchedule(new java.sql.Date(date.getTime()),menu);

        if (isInserted) {
            return Response.status(Response.Status.CREATED)
                    .entity("Lunch schedule added successfully.")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to add lunch schedule.")
                    .build();
        }
    }

    @POST
    @Path("/deletelunch")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLunch(LunchSchedule lunchSchedule) throws SQLException {
        // Extract data from the request body
        Date date = lunchSchedule.getLunchDate();

        // 1. Validate input
        if (date == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be empty!")
                    .build();
        }

        Date today = new Date();
        if (date.before(today)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Lunch date cannot be in the past!")
                    .build();
        }

        // Check if lunch schedule exists for the given date
        boolean lunchDateEmpty = LunchScheduleDAO.checkLunchScheduleAvailability(new java.sql.Date(date.getTime()));

        if (lunchDateEmpty) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No lunch schedule exists for the given date!")
                    .build();
        }

        // Proceed with deleting the lunch schedule entry
        boolean isDeleted = LunchScheduleDAO.deleteLunchSchedule(new java.sql.Date(date.getTime()));

        if (isDeleted) {
            return Response.status(Response.Status.OK)
                    .entity("Lunch schedule deleted successfully.")
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to delete lunch schedule.")
                    .build();
        }
    }

    @GET
    @Path("/viewlunchcurrmonth")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewLunchScheduleForThisMonth() {
        try {
            // Fetch the lunch schedule for this month
            List<LunchSchedule> lunchSchedules = LunchScheduleDAO.viewLunchScheduleForThisMonth();

            // If no schedules are found, return a 404 response
            if (lunchSchedules.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("No lunch schedules found for this month.")
                        .build();
            }

            // Return the list of lunch schedules
            return Response.status(Response.Status.OK)
                    .entity(lunchSchedules)
                    .build();

        } catch (SQLException e) {
            // Handle any SQL exceptions
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching lunch schedules for this month.")
                    .build();
        }
    }

}
