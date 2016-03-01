package router;

import builders.Request;
import builders.Response;
import handlers.RequestHandler;

public class Route implements RequestHandler {
    private RequestHandler handler;

    public Route(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public Response handle(Request request) {
        return handler.handle(request);
    }
}
