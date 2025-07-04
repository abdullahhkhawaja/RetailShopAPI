package resources;

import com.google.common.reflect.TypeToken;
import domain.Order;
import domain.Product;
import services.OrderDAO;
import services.ProductDAO;
import utils.JWTUtility;
import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/products")
public class ProductResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@HeaderParam("Authorization") String authHeader) throws SQLException {

        if (authHeader != null && authHeader.startsWith("Bearer "))
       {String token = authHeader.substring(7);

            if (JWTUtility.validateToken(token)) {

                List<Product> products = ProductDAO.GetAllProducts();
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
    public Response createOrder(@HeaderParam("Authorization") String authHeader, String payload) throws SQLException {

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

        List<Order> orders = new Gson().fromJson(payload, new TypeToken<List<Order>>() {}.getType());

        String username = JWTUtility.getUsernameFromToken(token);


        for (Order order : orders) {

            if (!order.getCustomerusername().equals(username)) {
                return Response.status(Response.Status.FORBIDDEN)
                        .entity("The username in the token does not match the order's username")
                        .build();
            }

            if (order.getProductId() < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid product ID for product ID: " + order.getProductId())
                        .build();
            }


            if (order.getOrderquantity() < 1) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Invalid order quantity for product ID: " + order.getProductId())
                        .build();
            }

            Product product = ProductDAO.GetSpecificProduct(order.getProductId());

            if (product == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Product not found with ID: " + order.getProductId())
                        .build();
            }

            int availableStock = product.getStockQuantity();
            if (availableStock < order.getOrderquantity()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("Insufficient stock for product ID: " + order.getProductId())
                        .build();
            }
        }

        for (Order order : orders) {

            boolean isCreated = OrderDAO.createOrder(order);

            if (!isCreated) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity("Failed to create order for product ID: " + order.getProductId())
                        .build();
            }
        }

        return Response.status(Response.Status.CREATED)
                .entity("All orders processed successfully")
                .build();
    }

}
