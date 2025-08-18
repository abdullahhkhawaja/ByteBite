//package resources;
//
//import domain.LeaveRequest;
//import services.LeaveRequestService;
//import services.SQSService;
//import services.SlackNotificationService;
//import services.UserService;
//
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.sql.SQLException;
//import java.util.Date;
//
//@Path("leaverequest")
//public class LeaveRequestResource {
//
//    @POST
//    @Path("createrequest")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response createrequest(LeaveRequest leaveRequest) throws Exception {
//
//        int userID = leaveRequest.getUserID();
//        Date leaveDate = leaveRequest.getLeaveDate();
//        String leaveReason = leaveRequest.getLeaveReason();
//
//        if (userID <= 0 || leaveDate == null || leaveReason == null || leaveReason.isEmpty()) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("User ID, Leave date, and reason cannot be empty!")
//                    .build();
//        }
//
//        Date today = new Date();
//        if (leaveDate.before(today)) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Leave date cannot be in the past!")
//                    .build();
//        }
//
//        boolean isRequestExist = LeaveRequestService.checkLeaveRequestExists(userID, new java.sql.Date(leaveDate.getTime()));
//
//        if (isRequestExist) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("A leave request already exists for this date!")
//                    .build();
//        }
//
//        boolean isInserted = LeaveRequestService.addLeaveRequest(userID, new java.sql.Date(leaveDate.getTime()), leaveReason);
//
//        if (isInserted) {
//
//            String userName = UserService.getUserNameFromID(userID);
//            String message = userName + " will be on leave on " + leaveDate;
//            SQSService sqsService = new SQSService();
//            sqsService.sendMessageToSQS(message);
//            sqsService.receiveMessageFromSQS();
//
//            return Response.status(Response.Status.CREATED)
//                    .entity("Leave request created successfully.")
//                    .build();
//        } else {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Failed to create leave request.")
//                    .build();
//        }
//    }
//
//}