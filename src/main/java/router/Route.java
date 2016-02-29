package router;

import builders.Request;
import builders.Response;
import handlers.RequestHandler;

import java.io.IOException;

public class Route implements RequestHandler {
    private String path;
    private RequestHandler handler;

    public Route(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public Response handle(Request request) throws IOException {
        return handler.handle(request);
    }
}
