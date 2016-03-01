package handlers;

import builders.Request;
import builders.Response;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedirectHandlerTest {
    ApplicationHandler handler = new RedirectHandler("/");

    @Test
    public void test() throws URISyntaxException {
        Request request = new Request("GET", new URI("/redirect"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.getStatusLine().contains("302 Found"));
        assertTrue(response.hasHeader("Location"));
        assertEquals("http://localhost:5000/", response.getHeaderValue("Location"));
    }
}
