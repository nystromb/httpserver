package builders;

import mocks.MockController;
import org.junit.Before;
import org.junit.Test;
import router.Route;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class RouteTest {
    @Before
    public void setUp(){

    }

    @Test
    public void testDoesMatch() throws URISyntaxException {
        Route route = new Route("/", new MockController());

        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        assertTrue(route.match(request.getPath()));
    }

    @Test
    public void testDoesNotMatch() throws URISyntaxException {
        Route route = new Route("/file1", new MockController());

        Request request = new Request("GET", new URI("/file1/something"), "HTTP/1.1");

        assertFalse(route.match(request.getPath()));
    }
    @Test
    public void testDoesNodfgtMatch() throws URISyntaxException {
        Route route = new Route("/file1", new MockController());

        Request request = new Request("GET", new URI("/file1"), "HTTP/1.1");

        assertTrue(route.match(request.getPath()));
    }
}
