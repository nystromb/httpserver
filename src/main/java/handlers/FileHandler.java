package handlers;

import builders.Request;
import builders.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler extends ApplicationController {
    private Response.Builder response = new Response.Builder();
    private Path publicDirectory;

    public FileHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get(publicDirectory.toString() + request.getPath())));

        if (request.hasHeader("Range")) {
            response.status(206);

            String ranges = request.getHeader("Range").split("=")[1];
            String[] range = ranges.split("-");

            if (ranges.startsWith("-")) {
                response.setBody(contents.substring(contents.length() - Integer.parseInt(range[1])));
            } else if (ranges.endsWith("-")) {
                response.setBody(contents.substring(Integer.parseInt(range[0])));
            } else {
                response.setBody(contents.substring(Integer.parseInt(range[0]), (Integer.parseInt(range[1]) + 1)));
            }

            return response.build();
        } else {
            response.status(200).setBody(contents);
        }

        return response.build();
    }

    @Override
    protected Response patch(Request request) throws IOException {
        Files.write(Paths.get(publicDirectory + request.getPath()), request.getBody().getBytes());
        return response.status(204).build();
    }
}
