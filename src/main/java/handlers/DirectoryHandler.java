package handlers;

import builders.Request;
import builders.Response;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryHandler extends ApplicationHandler {
    private Path publicDirectory;

    public DirectoryHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) {
        Path directory = Paths.get(publicDirectory.toString() + request.getPath());

        String contents = "<!DOCTYPE html><html><head></head><body><ul>";
        for (String file : directory.toFile().list()) {
            Path linkToFile = Paths.get(publicDirectory.resolveSibling(request.getPath()) + "/" + file).normalize();
            contents += "<li><a href=\"" + linkToFile +"\">" + file + "</a></li>";
        }
        contents += "</ul></body>";

        return new Response.Builder(200, contents).build();
    }
}
