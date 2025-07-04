package resources;

import domain.Customer;
import org.mindrot.jbcrypt.BCrypt;
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

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username or password is missing!").build();
        }

        Customer customer = CustomerDAO.validateLogin(username, password);

        if (customer != null) {
            String token = JWTUtility.generateToken(username);
            customer.setPassword("*********");

            return Response.ok()
                    .header("Authorization", "Bearer " + token)
                    .entity(customer + "\n\nLogin Successful!")
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

        if (username == null || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Username or password is missing").build();
        }

        boolean doesUsernameExist = CustomerDAO.checkUsernameAvailability(username);
        if (doesUsernameExist) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username is already taken.")
                    .build();
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        boolean inserted = CustomerDAO.SignUp(username, hashedPassword);

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
