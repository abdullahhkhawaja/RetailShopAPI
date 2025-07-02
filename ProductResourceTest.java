package resources;

import com.google.gson.Gson;
import domain.Order;
import domain.Product;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import services.OrderDAO;
import services.ProductDAO;
import utils.JWTUtility;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductResourceTest {

    private ProductResource productResource;

    @BeforeEach
    void setUp() {
        productResource = new ProductResource();
    }

    @Test
    void testGetAllProducts_ValidToken_ReturnsProducts() {
        String token = "valid.jwt.token";
        String authHeader = "Bearer " + token;
        List<Product> mockProducts = Arrays.asList(new Product(), new Product());

        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class);
             MockedStatic<ProductDAO> daoMock = mockStatic(ProductDAO.class)) {

            jwtMock.when(() -> JWTUtility.validateToken(token)).thenReturn(true);
            daoMock.when(ProductDAO::getAllProducts).thenReturn(mockProducts);

            Response response = productResource.getAllProducts(authHeader);
            assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
            assertEquals(mockProducts, response.getEntity());
        }
    }

    @Test
    void testGetAllProducts_InvalidToken_ReturnsUnauthorized() {
        String authHeader = "Bearer invalid.token";

        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class)) {
            jwtMock.when(() -> JWTUtility.validateToken("invalid.token")).thenReturn(false);

            Response response = productResource.getAllProducts(authHeader);
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertEquals("Invalid or expired token", response.getEntity());
        }
    }

    @Test
    void testGetAllProducts_MissingAuthHeader_ReturnsBadRequest() {
        Response response = productResource.getAllProducts(null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Authorization header is missing or invalid", response.getEntity());
    }

    @Test
    void testCreateOrder_ValidTokenAndMatchingUsername_ReturnsCreated() {
        String token = "valid.token";
        String authHeader = "Bearer " + token;
        String username = "john";

        Order order = new Order();
        order.setCustomerusername(username);
        String payload = new Gson().toJson(order);

        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class);
             MockedStatic<OrderDAO> daoMock = mockStatic(OrderDAO.class)) {

            jwtMock.when(() -> JWTUtility.validateToken(token)).thenReturn(true);
            jwtMock.when(() -> JWTUtility.getUsernameFromToken(token)).thenReturn(username);
            daoMock.when(() -> OrderDAO.createOrder(order)).thenReturn(true);

            Response response = productResource.createOrder(authHeader, payload);
            assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
            assertEquals(order, response.getEntity());
        }
    }

    @Test
    void testCreateOrder_UsernameMismatch_ReturnsForbidden() {
        String token = "valid.token";
        String authHeader = "Bearer " + token;

        Order order = new Order();
        order.setCustomerusername("alice"); // mismatch
        String payload = new Gson().toJson(order);

        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class)) {
            jwtMock.when(() -> JWTUtility.validateToken(token)).thenReturn(true);
            jwtMock.when(() -> JWTUtility.getUsernameFromToken(token)).thenReturn("bob");

            Response response = productResource.createOrder(authHeader, payload);
            assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
            assertEquals("The username in the token does not match the order's username", response.getEntity());
        }
    }

    @Test
    void testCreateOrder_InvalidToken_ReturnsUnauthorized() {
        String authHeader = "Bearer invalid.token";
        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class)) {
            jwtMock.when(() -> JWTUtility.validateToken("invalid.token")).thenReturn(false);

            Response response = productResource.createOrder(authHeader, "{}");
            assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
            assertEquals("Invalid or expired JWT token", response.getEntity());
        }
    }

    @Test
    void testCreateOrder_MissingAuthHeader_ReturnsUnauthorized() {
        Response response = productResource.createOrder(null, "{}");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Authorization header is missing or invalid", response.getEntity());
    }

    @Test
    void testCreateOrder_FailedOrderCreation_ReturnsServerError() {
        String token = "valid.token";
        String authHeader = "Bearer " + token;
        String username = "john";

        Order order = new Order();
        order.setCustomerusername(username);
        String payload = new Gson().toJson(order);

        try (MockedStatic<JWTUtility> jwtMock = mockStatic(JWTUtility.class);
             MockedStatic<OrderDAO> daoMock = mockStatic(OrderDAO.class)) {

            jwtMock.when(() -> JWTUtility.validateToken(token)).thenReturn(true);
            jwtMock.when(() -> JWTUtility.getUsernameFromToken(token)).thenReturn(username);
            daoMock.when(() -> OrderDAO.createOrder(order)).thenReturn(false);

            Response response = productResource.createOrder(authHeader, payload);
            assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
            assertEquals("Failed to create the order.", response.getEntity());
        }
    }
}
