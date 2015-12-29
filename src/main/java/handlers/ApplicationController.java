package handlers;

import builders.Request;
import builders.Response;

import java.io.IOException;

public abstract class ApplicationController implements RequestHandler {
    protected Response response = new Response.Builder(405, "Method Not Allowed").build();

    @Override
    public Response handle(Request request) throws IOException {

        switch (request.getMethod()) {
            case "GET":
                response = get(request);
                break;
            case "HEAD":
                response = head(request);
                break;
            case "POST":
                response = post(request);
                break;
            case "PUT":
                response = put(request);
                break;
            case "DELETE":
                response = delete(request);
                break;
            case "OPTIONS":
                response = options(request);
                break;
            case "PATCH":
                response = patch(request);
                break;
        }

        return response;
    }

    protected Response get(Request request) throws IOException {
        return response;
    }

    protected Response head(Request request) {
        return response;
    }

    protected Response post(Request request) {
        return response;
    }

    protected Response put(Request request) {
        return response;
    }

    protected Response delete(Request request) {
        return response;
    }

    protected Response options(Request request) {
        return response;
    }

    protected Response patch(Request request) throws IOException {
        return response;
    }
}
