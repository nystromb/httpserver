package handlers;

import builders.Request;
import builders.Response;
import mocks.MockController;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ApplicationControllerTest {

    @Before
    public void setUp(){

    }

    @Test
    public void testGetRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("200"));
    }


    @Test
    public void testPostRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("POST", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void testPutRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("PUT", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void testDeleteRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("DELETE", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void testOptionsRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("OPTIONS", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void testPatchRequest() throws URISyntaxException, IOException {
        ApplicationController app = new MockController();
        Request request = new Request("PATCH", new URI("/"), "HTTP/1.1");

        Response response = app.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void test() throws IOException, URISyntaxException {
        FileHandler app = new FileHandler();
        Request request = new Request("PUT", new URI("/file1"), "HTTP/1.1");

        Response response = app.handle(request);

        assertEquals("HTTP/1.1 405 Method Not Allowed", response.statusLine);
    }
}
