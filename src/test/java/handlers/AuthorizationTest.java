package handlers;

import builders.Request;
import builders.Response;
import mocks.MockHandler;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthorizationTest {
    Authorization handler = new Authorization("admin", "hunter2", "secretKey", new MockHandler());

    @Test
    public void testWithoutAuthorization() throws URISyntaxException {
        Request request = new Request("GET", new URI("/logs"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.getStatusLine().contains("401"));
        assertTrue(response.hasHeader("WWW-Authenticate"));
    }

    @Test
    public void testWithAuthorization() throws URISyntaxException {
        Request request = new Request("GET", new URI("/logs"), "HTTP/1.1");
        request.addHeader("Authorization", "Basic YWRtaW46aHVudGVyMg==");

        Response response = handler.handle(request);

        assertEquals("HTTP/1.1 200 OK", response.getStatusLine());
    }
}
