package handlers;

import builders.Request;
import builders.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class DirectoryHandler extends ApplicationController {
    private Path publicDirectory;

    public DirectoryHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) throws IOException {
        String contents = "<!DOCTYPE html><html><head></head><body><ul>";
        for (String file : new File(publicDirectory.toString(), request.getPath()).list()) {
            contents += "<li><a href=\"/" + file +"\">" + file + "</a></li>";
        }
        contents += "</ul></body>";
        return new Response.Builder(200, contents).build();
    }
}
