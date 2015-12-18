package mocks;

import builders.Request;
import builders.Response;
import handlers.ApplicationController;

import java.io.IOException;

public class MockRedirect extends ApplicationController {
    String redirectPath;

    public MockRedirect(String redirectPath) {
        this.redirectPath = redirectPath;
    }


    @Override
    protected Response get(Request request) throws IOException {
        return new Response.Builder(302).build();
    }
}
