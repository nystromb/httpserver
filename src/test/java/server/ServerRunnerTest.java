package server;

import configuration.Settings;
import handlers.Authorization;
import mocks.MockController;
import mocks.MockRedirect;
import mocks.MockSocket;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import router.Route;
import router.Router;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;

import static org.junit.Assert.assertTrue;

public class ServerRunnerTest {
    OutputStream output;

    @Before
    public void setUp() throws IOException {
        createDirectories();
        Router.addRoute(new Route("/", new MockController()));
        Router.addRoute(new Route("/method_options", new MockController()));
        Router.addRoute(new Route("/logs", new Authorization("admin", "hunter2", "secret", new MockController())));
        Router.addRoute(new Route("/redirect", new MockRedirect("/")));
        Router.addRoute(new Route("/parameters", new MockController()));
        Router.addRoute(new Route("/file1", new MockController()));
        Router.addRoute(new Route("/file2", new MockController()));
        Router.addRoute(new Route("/image.png", new MockController()));
        Router.addRoute(new Route("/image.jpeg", new MockController()));
        Router.addRoute(new Route("/image.gif", new MockController()));
        Router.addRoute(new Route("/text-file.txt", new MockController()));
        Router.addRoute(new Route("/partial_content.txt", new MockController()));
        Router.addRoute(new Route("/patch_content.txt", new MockController()));
        output = new ByteArrayOutputStream();
        deleteCreatedFiles();
    }

    @After
    public void shutDown() {
        Router.clearAll();
    }

    private void createDirectories() throws IOException {
        File publicDir = new File(System.getProperty("user.dir"), "/public/");
        publicDir.mkdir();
        Files.createFile(new File(Settings.PUBLIC_DIR, "/file1").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/file2").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/text-file.txt").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/image.png").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/image.gif").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/partial_content.txt").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/patch-content.txt").toPath());
        Files.createFile(new File(Settings.PUBLIC_DIR, "/image.jpeg").toPath());
    }


    private void deleteCreatedFiles() throws IOException {
        Files.delete(new File(Settings.PUBLIC_DIR, "/file1").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/file2").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/text-file.txt").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/image.png").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/image.gif").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/partial_content.txt").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/patch-content.txt").toPath());
        Files.delete(new File(Settings.PUBLIC_DIR, "/image.jpeg").toPath());
    }

    @Test
    public void testRootReturns200OK() throws URISyntaxException {
        String request = "GET / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testUndefinedRouteNotFound() throws URISyntaxException {
        String request = "GET /foobar HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("404 Not Found"));
    }

    @Test
    public void testLogsReturns401Unauthorized() throws URISyntaxException {
        String request = "GET /logs HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("401 Unauthorized"));
    }


    @Test
    public void testLogsReturns200OK() throws URISyntaxException {
        String request = "GET /logs HTTP/1.1\r\nAuthorization: Basic YWRtaW46aHVudGVyMg==\r\n\r\n";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testPostToRootNotAllowed() throws URISyntaxException {
        String request = "POST / HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }

    @Test
    public void testGetsRedirect302() throws URISyntaxException {
        String request = "GET /redirect HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("302 Found"));
    }

    @Test
    public void testMethodOptions() throws URISyntaxException {
        String request = "GET /method_options HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testParameters() throws URISyntaxException {
        String request = "GET /parameters HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testFile1GetsContents() throws URISyntaxException {
        String request = "GET /file1 HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsPNGContents() throws URISyntaxException {
        String request = "GET /image.png HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsJPEGContents() throws URISyntaxException {
        String request = "GET /image.jpeg HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsGIFContents() throws URISyntaxException {
        String request = "GET /image.gif HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsTextFileContents() throws URISyntaxException {
        String request = "GET /text-file.txt HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsPartialContents() throws URISyntaxException {
        String request = "GET /partial_content.txt HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsPatchContentFile() throws URISyntaxException {
        String request = "GET /patch_content.txt HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testGetsFile2OK() throws URISyntaxException {
        String request = "GET /file2 HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("200 OK"));
    }

    @Test
    public void testPostsFile2NotAllowed() throws URISyntaxException {
        String request = "POST /file1 HTTP/1.1";
        MockSocket client = new MockSocket(new ByteArrayInputStream(request.getBytes()), output);

        new ServerRunner(client).run();

        assertTrue(output.toString().contains("405 Method Not Allowed"));
    }
}
