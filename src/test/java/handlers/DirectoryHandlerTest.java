package handlers;

import builders.Request;
import builders.Response;
import configuration.HTTPConfiguration;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class DirectoryHandlerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    ApplicationController handler = new DirectoryHandler(config.getPublicDirectory());

    @Test
    public void testReturns200OK() throws URISyntaxException, IOException {
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("200 OK"));
    }
}
