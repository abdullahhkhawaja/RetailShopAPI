package resources;

import domain.Customer;
import services.CustomerDAO;
import utils.JWTUtility;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("/login")
public class LoginResource {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public static Response login(Customer loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Customer customer = CustomerDAO.validateLogin(username, password);

        if (customer != null) {
            String token = JWTUtility.generateToken(username);
            customer.setPassword(null);

            return Response.ok()
                    .header("Authorization", "Bearer " + token)
                    .entity(customer)
                    .build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid username or password.")
                    .build();
        }
    }

    @POST
    @Path("/signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signup(Customer customer) throws SQLException {
        String username = customer.getUsername();
        String password = customer.getPassword();

        boolean doesUsernameExist = CustomerDAO.checkUsernameAvailability(username);
        if (doesUsernameExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username is already taken.")
                    .build();
        }

        boolean inserted = CustomerDAO.SignUp(username, password);

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
}
