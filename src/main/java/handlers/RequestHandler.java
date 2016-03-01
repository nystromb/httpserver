package handlers;

import builders.Request;
import builders.Response;

public interface RequestHandler {
    Response handle(Request request);
}
