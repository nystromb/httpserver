package handlers;

import builders.Request;
import builders.Response;

import java.io.IOException;

public interface RequestHandler {
    Response handle(Request request) throws IOException;
}
