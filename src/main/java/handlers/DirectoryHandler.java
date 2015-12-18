package handlers;

import builders.Request;
import builders.Response;
import configuration.Settings;

import java.io.File;
import java.io.IOException;

public class DirectoryHandler extends ApplicationController {

    @Override
    protected Response get(Request request) throws IOException {
        String contents = "<!DOCTYPE html><html><head></head><body><ul>";
        for(String file : new File(Settings.PUBLIC_DIR, request.getPath()).list()){
            contents += "<li><a href=\"/" + file +"\">" + file + "</a></li>";
        }
        contents += "</ul></body>";
        return new Response.Builder(200, contents).build();
    }
}
