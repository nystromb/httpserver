package handlers;

import builders.Request;
import builders.Response;
import configuration.Settings;
import handlers.FileHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class FileHandlerTest {
    FileHandler handler = new FileHandler();
    File publicDir = new File(System.getProperty("user.dir"), "/public/");
    @Before
    public void setUp(){
        publicDir.mkdir();
    }

    private void createIfNotExists(String path, String contents) throws IOException {
        if(Files.notExists(new File(Settings.PUBLIC_DIR, path).toPath())){
            Path file = Files.createFile(new File(Settings.PUBLIC_DIR, path).toPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file.toFile()));
            writer.write(contents);
            writer.close();
        }
    }

    @After
    public void shutDown() throws IOException {
        for(File file : publicDir.listFiles()){
            Files.delete(file.toPath());
        }
        Files.deleteIfExists(publicDir.toPath());
    }

    @Test
    public void testReturns200OK() throws URISyntaxException, IOException {
        Request request = new Request("GET", new URI("/file1"), "HTTP/1.1");

        createIfNotExists(request.getPath(), "");
        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("200 OK"));
    }

    @Test
    public void testReturnsFile1Contents() throws URISyntaxException, IOException {
        Request request = new Request("GET", new URI("/file1"), "HTTP/1.1");

        createIfNotExists(request.getPath(), "file1 contents");
        Response response = handler.handle(request);

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

//    @Test
//    public void testRangeHeaderReturns206Code() throws URISyntaxException, IOException {
//        Request request = new Request("GET", new URI("/partial_content.txt"), "HTTP/1.1");
//        request.addHeader("Range", "bytes=0-4");
//
//        createIfNotExists(request.getPath(), "");
//        Response response = handler.handle(request);
//
//        assertTrue(response.statusLine.contains("206"));
//    }

//    @Test
//    public void testRangeHeaderReturnsPartialFileContentsRange04() throws URISyntaxException, IOException {
//        Request request = new Request("GET", new URI("/partial_content.txt"), "HTTP/1.1");
//        request.addHeader("Range", "bytes=0-4");
//
//        createIfNotExists(request.getPath(), "This is a file that contains text to read part of in order to fulfill a 206.\n");
//        Response response = handler.handle(request);
//
//        assertArrayEquals("This ".getBytes(), response.body);
//    }
//
//    @Test
//    public void testRangeHeaderReturnsPartialFileContentsStart4() throws URISyntaxException, IOException {
//        Request request = new Request("GET", new URI("/partial_content.txt"), "HTTP/1.1");
//        request.addHeader("Range", "bytes=4-");
//
//        createIfNotExists(request.getPath(), "This is a file that contains text to read part of in order to fulfill a 206.\n");
//        Response response = handler.handle(request);
//
//        assertTrue(new String(response.toByteArray()).endsWith(" is a file that contains text to read part of in order to fulfill a 206.\n"));
//    }
//
//
//    @Test
//    public void testRangeHeaderReturnsPartialFileContentsEnd6() throws URISyntaxException, IOException {
//        Request request = new Request("GET", new URI("/partial_content.txt"), "HTTP/1.1");
//        request.addHeader("Range", "bytes=-6");
//
//        createIfNotExists(request.getPath(), "This is a file that contains text to read part of in order to fulfill a 206.\n");
//        Response response = handler.handle(request);
//
//        assertEquals(new String(response.body), " 206.\n");
//    }

    @Test
    public void testPatchContent() throws URISyntaxException, IOException {
        Request request = new Request("PATCH", new URI("/patch-content.txt"), "HTTP/1.1");
        request.setBody("patched content");
        request.addHeader("ETag", "dc50a0d27dda2eee9f65644cd7e4c9cf11de8bec");

        createIfNotExists(request.getPath(), "patched content");
        Response response = handler.handle(request);

        assertTrue(response.statusLine.contains("204"));
        assertFalse(new String(response.toByteArray()).contains("patched content"));

        request = new Request("GET", new URI("/patch-content.txt"), "HTTP/1.1");

        response = handler.handle(request);

        assertTrue(new String(response.toByteArray()).contains("patched content"));
    }
}
