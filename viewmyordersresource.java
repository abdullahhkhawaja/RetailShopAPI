package resources;

import utils.JWTUtility;
import domain.Order;
import services.OrderDAO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/viewmyorders")
public class viewmyordersresource {

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewMyOrders(@HeaderParam("Authorization") String authHeader, @PathParam("username") String username) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Authorization header is missing or invalid")
                    .build();
        }

        String token = authHeader.substring(7);

        if (!JWTUtility.validateToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid or expired JWT token")
                    .build();
        }

        String tokenUsername = JWTUtility.getUsernameFromToken(token);

        if (!username.equals(tokenUsername)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("Username in token does not match path parameter")
                    .build();
        }

        List<Order> orders = OrderDAO.getOrdersByUsername(username);

        if (orders.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No orders found for the user: " + username)
                    .build();
        } else {
            return Response.status(Response.Status.OK)
                    .entity(orders)
                    .build();
        }
    }
}