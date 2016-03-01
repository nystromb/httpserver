package handlers;

import builders.Request;
import builders.Response;
import configuration.HTTPConfiguration;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FileHandlerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    FileHandler handler = new FileHandler(config.getPublicDirectory());
    String fileContents = "This is a file that contains text to read part of in order to fulfill a 206.\n";

    private Path createTempFile(String fileName) throws IOException {
        Path file = Files.createTempFile(config.getPublicDirectory(), fileName, ".txt");
        file.toFile().deleteOnExit();
        return file;
    }

    private void writeTo(Path filePath, String contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        writer.write(contents);
        writer.close();
    }

    @Test
    public void testReturns200OK() throws URISyntaxException, IOException {
        Path filePath = createTempFile("file1");
        writeTo(filePath, "file1 contents");
        Request request = new Request("GET", new URI("/" + filePath.toFile().getName()), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("200 OK"));
        assertEquals("file1 contents", new String(response.body));
    }

    @Test
    public void testMethodNotAllowedForPostRequest() throws IOException, URISyntaxException {
        Request request = new Request("POST", new URI("/file1"), "HTTP/1.1");
        
        Response response = handler.handle(request);
        
        assertTrue(response.statusLine.contains("405"));
    }
    
    @Test
    public void testMethodNotAllowedForPutRequest() throws IOException, URISyntaxException {
        Request request = new Request("PUT", new URI("/file1"), "HTTP/1.1");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("405"));
    }

    @Test
    public void testRangeHeaderReturns206Code() throws URISyntaxException, IOException {
        Path file = createTempFile("partial_content");
        writeTo(file, fileContents);
        Request request = new Request("GET", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        request.addHeader("Range", "bytes=0-4");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("206"));
    }

    @Test
    public void testRangeHeaderReturnsPartialFileContentsRange04() throws URISyntaxException, IOException {
        Path file = createTempFile("partial_content");
        writeTo(file, fileContents);
        Request request = new Request("GET", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        request.addHeader("Range", "bytes=0-4");

        Response response = handler.handle(request);

        assertArrayEquals("This ".getBytes(), response.body);
    }

    @Test
    public void testRangeHeaderReturnsPartialFileContentsStart4() throws URISyntaxException, IOException {
        Path file = createTempFile("partial_content");
        writeTo(file, fileContents);
        Request request = new Request("GET", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        request.addHeader("Range", "bytes=4-");

        Response response = handler.handle(request);

        assertEquals(" is a file that contains text to read part of in order to fulfill a 206.\n", new String(response.body));
    }


    @Test
    public void testRangeHeaderReturnsPartialFileContentsEnd6() throws URISyntaxException, IOException {
        Path file = createTempFile("partial_content");
        writeTo(file, fileContents);
        Request request = new Request("GET", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        request.addHeader("Range", "bytes=-6");

        Response response = handler.handle(request);

        assertEquals(" 206.\n", new String(response.body));
    }

    @Test
    public void testPatchContent() throws URISyntaxException, IOException {
        Path file = createTempFile("patched_content");
        Request request = new Request("PATCH", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        request.setBody("patched content");
        request.addHeader("ETag", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");

        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("204"));
        assertFalse(new String(response.toByteArray()).contains("patched content"));

        request = new Request("GET", new URI("/" + file.toFile().getName().toString()), "HTTP/1.1");
        response = handler.handle(request);

        assertTrue(new String(response.toByteArray()).contains("patched content"));
    }
}
