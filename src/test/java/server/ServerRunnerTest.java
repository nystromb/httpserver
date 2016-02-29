package server;

import configuration.HTTPConfiguration;
import handlers.ApplicationController;
import handlers.Authorization;
import handlers.FileHandler;
import mocks.MockController;
import mocks.MockSocket;
import org.junit.Before;
import org.junit.Test;
import router.Route;
import router.Router;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static org.junit.Assert.assertTrue;

public class ServerRunnerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    OutputStream output;

    @Before
    public void setUp() throws IOException {
        output = new ByteArrayOutputStream();
    }

    private Path createTempFile(String fileName, String ext) throws IOException {
        if (ext == null)
            ext = ".txt";
        Path file = Files.createTempFile(config.getPublicDirectory(), fileName, ext);
        file.toFile().deleteOnExit();
        return file;
    }

    private void writeTo(Path filePath, String contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        writer.write(contents);
        writer.close();
    }

    @Test
    public void testRootReturns200OK() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "GET / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testUndefinedRouteNotFound() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "GET /foobar HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("404 Not Found"));
    }

    @Test
    public void testLogsReturns401Unauthorized() throws URISyntaxException {
        ApplicationController handler = new FileHandler(config.getPublicDirectory());
        Route logsRoute = new Route(new Authorization("admin","hunter2","secret", handler));
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/logs", logsRoute);
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /logs HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("401 Unauthorized"));
    }

    @Test
    public void testLogsReturns200OK() throws URISyntaxException {
        ApplicationController handler = new MockController(config.getPublicDirectory());
        Route logsRoute = new Route(new Authorization("admin","hunter2","secret", handler));
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/logs", logsRoute);
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /logs HTTP/1.1\r\nAuthorization: Basic YWRtaW46aHVudGVyMg==\r\n\r\n";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testPostToRootDirectoryNotAllowed() throws URISyntaxException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        String request = "POST / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testPostsToFileNotAllowed() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = createTempFile("file1", null);
        String request = "POST /" + file.toFile().getName().toString() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testMethodOptionsRoute() throws URISyntaxException {
        HashMap<String, Route> routes = new HashMap<>();
        routes.put("/method_options", new Route(new MockController()));
        Router router = new Router(config.getPublicDirectory(), routes);
        String request = "GET /method_options HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsFileContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = createTempFile("file1", null);
        writeTo(file, "file1 contents");
        String request = "GET /" + file.toFile().getName().toString() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
        assertTrue(output.toString().contains("file1 contents"));
    }

    @Test
    public void testGetsPNGContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = createTempFile("image", ".png");
        String request = "GET /" + file.toFile().getName().toString() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsJPEGContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = createTempFile("image", ".jpeg");
        String request = "GET /" + file.toFile().getName().toString() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsGIFContents() throws URISyntaxException, IOException {
        Router router = new Router(config.getPublicDirectory(), new HashMap<>());
        Path file = createTempFile("image", ".gif");
        String request = "GET /" + file.toFile().getName().toString() + " HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client, router).run();

        assertTrue(output.toString().contains("200 OK"));
    }
}
