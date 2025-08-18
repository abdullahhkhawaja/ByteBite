import common.AuthHeader;
import org.junit.jupiter.api.Test;
import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.*;

class AuthHeaderTest {

    @Test
    void testMissingHeader() {
        Response response = AuthHeader.validateHeader(null);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Authorization header is missing or empty.", response.getEntity());
    }

    @Test
    void testEmptyHeader() {
        Response response = AuthHeader.validateHeader("");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Authorization header is missing or empty.", response.getEntity());
    }

    @Test
    void testInvalidFormat_NoBearer() {
        Response response = AuthHeader.validateHeader("Token abcdef");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Invalid Authorization format. Expected 'Bearer <token>'", response.getEntity());
    }

    @Test
    void testInvalidFormat_TooShort() {
        Response response = AuthHeader.validateHeader("Bearer ");
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("Invalid Authorization format. Expected 'Bearer <token>'", response.getEntity());
    }

    @Test
    void testValidHeader() {
        Response response = AuthHeader.validateHeader("Bearer mytoken123");
        assertNull(response); // currently your method returns null for valid headers
    }
}
