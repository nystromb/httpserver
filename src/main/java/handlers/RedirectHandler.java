package handlers;

import builders.Request;
import builders.Response;

public class RedirectHandler extends ApplicationController {
    private Response.Builder response = new Response.Builder(302);
    String redirectPath;

    public RedirectHandler(String redirectPath) {
        this.redirectPath = redirectPath;
    }

    @Override
    protected Response get(Request request) {
        return response.addHeader("Location", "http://localhost:5000" + redirectPath).build();
    }
}
