package resources;

import domain.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LoginResourceTest {

    public LoginResource loginResource = new LoginResource();

    @BeforeEach
    public void setUp()
    {

        loginResource = new LoginResource();
    }

    @Test
    public void testLoginValidUser() {                                       //valid login
        Customer customer = new Customer(1,"abdullahkhawaja", "password");

        Response response = loginResource.login(customer);

        assertEquals(200, response.getStatus());

        MultivaluedMap<String, Object> headers = response.getMetadata();

        Object authHeader = headers.getFirst("Authorization");
        assertNotNull(authHeader, "Authorization header should not be null");

        String token = authHeader.toString();
        assertTrue(token.startsWith("Bearer "), "Token should start with 'Bearer '");
    }


    @Test
    public void testLoginInvalidUsername() {                                       //invalid login
        Customer customer = new Customer();
        customer.setUsername("meowmeow");
        customer.setPassword("password");

        Response response = loginResource.login(customer);

        assertEquals(401, response.getStatus());

        String errorMessage = response.getEntity().toString();
        System.out.println(errorMessage);
    }

    @Test
    public void testLoginInvalidPassword() {
        Customer customer = new Customer();
        customer.setUsername("abdullahkhawaja");
        customer.setPassword("passwor");

        Response response = loginResource.login(customer);
        assertEquals(401, response.getStatus());
        String errorMessage = response.getEntity().toString();
        System.out.println(errorMessage);

    }

    @Test
    public void testSignupValidUser() throws SQLException {        //change username to run it again
        Customer customer = new Customer();
        customer.setUsername("HELLO1234");
        customer.setPassword("password");

        Response response = loginResource.signup(customer);

        assertEquals(201, response.getStatus());

        String message  = response.getEntity().toString();
        System.out.println(message);

    }

    @Test
    public void testSignupInvalidUser() throws SQLException {
        Customer customer = new Customer();
        customer.setUsername("abdullahkhawaja");
        customer.setPassword("password");

        Response response = loginResource.signup(customer);

        assertEquals(400, response.getStatus());

        String errorMessage = response.getEntity().toString();
        System.out.println(errorMessage);

    }


}

