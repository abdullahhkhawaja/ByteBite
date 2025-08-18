//package resources;
//
//import services.HeadcountService;
//
//import javax.ws.rs.*;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.sql.Date;
//import java.sql.SQLException;
//
//@Path("/headcount")
//public class HeadcountResource {
//
//    @GET
//    @Path("/day")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getHeadcountForDay(@QueryParam("date") String dateString) {
//        try {
//            // Debugging: Check the received date string
//            System.out.println("Received date string: " + dateString);
//
//            // Parse the input date string into a java.util.Date object
//            java.util.Date utilDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(dateString);
//            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());  // Convert to java.sql.Date
//
//            // Check if the date is correctly parsed
//            System.out.println("Parsed date: " + sqlDate);
//
//            // Calculate the headcount for the given day
//            int headcount = HeadcountService.calculateHeadcountForDay(utilDate);
//
//            // Return the headcount as a JSON response
//            return Response.status(Response.Status.OK)
//                    .entity("{\"headcount\": " + headcount + "}")
//                    .build();
//
//        } catch (Exception e) {
//            // Log error and return a bad request response
//            System.out.println("Error while parsing date: " + e.getMessage());
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Invalid date format. Please use 'yyyy-MM-dd'.")
//                    .build();
//        }
//    }
//}
