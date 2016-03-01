package handlers;

import builders.Request;
import builders.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler extends ApplicationController {
    private Response.Builder response = new Response.Builder(200);
    private Path publicDirectory;

    public FileHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(publicDirectory.toString() + request.getPath())));

        if (request.hasHeader("Range")) {
            return new PartialContentHandler(contents).handle(request);
        }

        return response.setBody(contents).build();
    }

    @Override
    protected Response patch(Request request) throws IOException {
        Files.write(Paths.get(publicDirectory + request.getPath()), request.getBody().getBytes());
        return response.status(204).build();
    }
}
