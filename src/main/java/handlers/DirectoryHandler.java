package handlers;

import builders.Request;
import builders.Response;

import java.io.File;
import java.nio.file.Path;

public class DirectoryHandler extends ApplicationHandler {
    private Path publicDirectory;

    public DirectoryHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) {
        File directory = new File(publicDirectory.toString(), request.getPath());
        String contents = "<!DOCTYPE html><html><head></head><body><ul>";
        for (String file : directory.list()) {
            contents += "<li><a href=\"/" + file +"\">" + file + "</a></li>";
        }
        contents += "</ul></body>";
        return new Response.Builder(200, contents).build();
    }
}
