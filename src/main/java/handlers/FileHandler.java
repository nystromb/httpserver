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
    protected Response get(Request request) {
        String contents = null;
        try {
            contents = new String(Files.readAllBytes(Paths.get(publicDirectory.toString() + request.getPath())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (request.hasHeader("Range")) {
            return new PartialContentHandler(contents).handle(request);
        }

        return response.setBody(contents).build();
    }

    @Override
    protected Response patch(Request request) {
        try {
            Files.write(Paths.get(publicDirectory + request.getPath()), request.getBody().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.status(204).build();
    }
}
