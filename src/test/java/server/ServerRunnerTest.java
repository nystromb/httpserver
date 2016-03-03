package server;

import configuration.HTTPConfiguration;
import handlers.ApplicationHandler;
import handlers.Authorization;
import handlers.FileHandler;
import helpers.SpecHelper;
import mocks.MockHandler;
import mocks.MockSocket;
import org.junit.Before;
import org.junit.Test;
import router.Route;
import router.Router;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class ServerRunnerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    Logger logger = Logger.getLogger("server.logs");
    SpecHelper testHelper = new SpecHelper();
    OutputStream output;

    @Before
    public void setUp() throws IOException {
        logger.setUseParentHandlers(false);
        output = new ByteArrayOutputStream();
    }

    @Test
    public void testRootReturns200OK() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "GET / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testUndefinedRouteNotFound() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "GET /foobar HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("404 Not Found"));
    }

    @Test
    public void testLogsReturns401Unauthorized() throws URISyntaxException {
        ApplicationHandler handler = new FileHandler(config.getPublicDirectory());
        Route logsRoute = new Route(new Authorization("admin","hunter2","secret", handler));
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/logs", logsRoute);
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /logs HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("401 Unauthorized"));
    }

    @Test
    public void testLogsReturns200OK() throws URISyntaxException {
        ApplicationHandler handler = new MockHandler(config.getPublicDirectory());
        Route logsRoute = new Route(new Authorization("admin","hunter2","secret", handler));
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/logs", logsRoute);
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /logs HTTP/1.1\r\nAuthorization: Basic YWRtaW46aHVudGVyMg==\r\n\r\n";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testPostToRootDirectoryNotAllowed() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "POST / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testPostsToFileNotAllowed() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = testHelper.createTempFile(config.getPublicDirectory(), "file1", null);
        String request = "POST /" + file.toFile().getName() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testMethodOptionsRoute() throws URISyntaxException {
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/method_options", new Route(new MockHandler()));
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /method_options HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsFileContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = testHelper.createTempFile(config.getPublicDirectory(), "file1", null);
        testHelper.writeTo(file, "file1 contents");
        String request = "GET /" + file.toFile().getName() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
        assertTrue(output.toString().contains("file1 contents"));
    }

    @Test
    public void testGetsPNGContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = testHelper.createTempFile(config.getPublicDirectory(), "image", ".png");
        String request = "GET /" + file.toFile().getName() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsJPEGContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = testHelper.createTempFile(config.getPublicDirectory(), "image", ".jpeg");
        String request = "GET /" + file.toFile().getName() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsGIFContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = testHelper.createTempFile(config.getPublicDirectory(), "image", ".gif");
        String request = "GET /" + file.toFile().getName() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router, logger).run();

        assertTrue(output.toString().contains("200 OK"));
    }
}
