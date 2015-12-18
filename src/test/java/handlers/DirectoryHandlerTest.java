package handlers;

import builders.Request;
import builders.Response;
import handlers.ApplicationController;
import mocks.MockController;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class DirectoryHandlerTest {
    ApplicationController handler = new MockController();

    @Before
    public void setUp() throws IOException {
        ;
    }

    @Test
    public void testReturns200OK() throws URISyntaxException, IOException {
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("200 OK"));
    }
}
