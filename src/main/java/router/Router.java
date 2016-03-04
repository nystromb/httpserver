package router;

import builders.Request;
import builders.Response;
import handlers.DirectoryHandler;
import handlers.FileHandler;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Router {
    private HashMap<String, Route> routes;
    private Path publicDirectory;

    public Router(Path publicDirectory, HashMap<String, Route> routes) {
        this.publicDirectory = publicDirectory;
        this.routes = routes;
    }

    public Response handle(Request request) {
        File requestedPath = Paths.get(publicDirectory.toString() + request.getPath()).toFile();

        if(requestedPath.isFile())
           return new FileHandler(publicDirectory).handle(request);
        else if (requestedPath.isDirectory()) {
            return new DirectoryHandler(publicDirectory).handle(request);
        } else if (routes.containsKey(request.getPath())) {
            return routes.get(request.getPath()).handle(request);
        }

        return new Response.Builder(404, "Not Found").build();
    }
}
