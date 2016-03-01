package handlers;

import builders.Request;
import builders.Response;
import configuration.HTTPConfiguration;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertTrue;

public class DirectoryHandlerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    ApplicationController handler = new DirectoryHandler(config.getPublicDirectory());

    @Test
    public void testReturns200OK() throws URISyntaxException {
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("200 OK"));
    }

    @Test
    public void testGetsListOfFilesInDirectory() throws URISyntaxException {
        String[] listOfFiles = config.getPublicDirectory().toFile().list();
        Request request = new Request("GET", new URI("/"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(new String(response.body).contains(listOfFiles[0]));
        assertTrue(new String(response.body).contains(listOfFiles[listOfFiles.length-1]));
    }

    @Test
    public void testNestedDirectory() throws URISyntaxException, IOException {
        Path directory = Files.createTempDirectory(config.getPublicDirectory(), "test_directory");
        directory.toFile().deleteOnExit();
        String [] dirPath = directory.toString().split("/");
        Path file = Files.createTempFile(Paths.get(config.getPublicDirectory() + "/" + dirPath[dirPath.length - 1]), "test_file", ".txt");
        file.toFile().deleteOnExit();
        Request request = new Request("GET", new URI("/"+dirPath[dirPath.length-1]), "HTTP/1.1");

        Response response = new DirectoryHandler(config.getPublicDirectory()).handle(request);

        assertTrue(new String(response.body).contains(file.toFile().getName().toString()));
    }
}
