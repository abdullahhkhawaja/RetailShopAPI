package resources;

import domain.Order;
import domain.Product;
import services.OrderDAO;
import services.ProductDAO;
import utils.JWTUtility;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/products")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@HeaderParam("Authorization") String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (JWTUtility.validateToken(token)) {
                List<Product> products = ProductDAO.getAllProducts();
                return Response.ok(products).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid or expired token")
                        .build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Authorization header is missing or invalid")
                    .build();
        }
    }

    @POST
    @Path("/createorder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrder(@HeaderParam("Authorization") String authHeader, String payload) {

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

        Order order = new Gson().fromJson(payload, Order.class);

        String username = JWTUtility.getUsernameFromToken(token);
        if (!order.getCustomerusername().equals(username)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("The username in the token does not match the order's username")
                    .build();
        }

        boolean isCreated = OrderDAO.createOrder(order);

        if (isCreated) {
            return Response.status(Response.Status.CREATED)
                    .entity(order)
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Failed to create the order.")
                    .build();
        }
    }


}
