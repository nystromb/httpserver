package handlers;

import builders.Request;
import builders.Response;
import mocks.MockHandler;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class ApplicationHandlerTest {

    @Test
    public void testGetRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("200"));
    }

    @Test
    public void testPostRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("POST", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("405"));
    }

    @Test
    public void testPutRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("PUT", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("405"));
    }

    @Test
    public void testDeleteRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("DELETE", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("405"));
    }

    @Test
    public void testOptionsRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("OPTIONS", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("405"));
    }

    @Test
    public void testPatchRequest() throws URISyntaxException {
        ApplicationHandler app = new MockHandler();
        Request request = new Request("PATCH", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.getStatusLine().contains("405"));
    }
}
