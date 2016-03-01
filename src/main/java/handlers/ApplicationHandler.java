package handlers;

import builders.Request;
import builders.Response;

public abstract class ApplicationHandler implements RequestHandler {
    protected Response methodNotAllowed = new Response.Builder(405, "Method Not Allowed").build();

    @Override
    public Response handle(Request request) {

        switch (request.getMethod()) {
            case "GET":
                methodNotAllowed = get(request);
                break;
            case "HEAD":
                methodNotAllowed = head(request);
                break;
            case "POST":
                methodNotAllowed = post(request);
                break;
            case "PUT":
                methodNotAllowed = put(request);
                break;
            case "DELETE":
                methodNotAllowed = delete(request);
                break;
            case "OPTIONS":
                methodNotAllowed = options(request);
                break;
            case "PATCH":
                methodNotAllowed = patch(request);
                break;
        }

        return methodNotAllowed;
    }

    protected Response get(Request request) {
        return methodNotAllowed;
    }

    protected Response head(Request request) {
        return methodNotAllowed;
    }

    protected Response post(Request request) {
        return methodNotAllowed;
    }

    protected Response put(Request request) {
        return methodNotAllowed;
    }

    protected Response delete(Request request) {
        return methodNotAllowed;
    }

    protected Response options(Request request) {
        return methodNotAllowed;
    }

    protected Response patch(Request request) {
        return methodNotAllowed;
    }
}
