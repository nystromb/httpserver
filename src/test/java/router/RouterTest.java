package router;

import handlers.DirectoryHandler;
import org.junit.Before;

public class RouterTest {
    Route rootRoute = new Route("/", new DirectoryHandler());

    @Before
    public void setUp(){
        Router.addRoute(rootRoute);
    }

//    @Test
//    public void test() throws URISyntaxException {
//        Request request = new Request("GET", new URI("/"), "HTTP/1.1");
//
//        Route route = Router.getRoute(request.getPath());
//
//        assertSame(rootRoute, route);
//    }
}
