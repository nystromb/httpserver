package handlers;

import builders.Request;
import builders.Response;
import mocks.MockController;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthorizationTest {
    Authorization handler = new Authorization("admin", "hunter2", "secretKey", new MockController());

    @Test
    public void testWithoutAuthorization() throws URISyntaxException, IOException {
        Request request = new Request("GET", new URI("/logs"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("401"));
        assertTrue(response.headers.containsKey("WWW-Authenticate"));
    }

    @Test
    public void testWithAuthorization() throws IOException, URISyntaxException {
        Request request = new Request("GET", new URI("/logs"), "HTTP/1.1");
        request.addHeader("Authorization", "Basic YWRtaW46aHVudGVyMg==");

        Response response = handler.handle(request);

        assertEquals("HTTP/1.1 200 OK", response.statusLine);
    }
}
