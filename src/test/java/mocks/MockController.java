package mocks;

import builders.Request;
import builders.Response;
import handlers.ApplicationController;

import java.io.IOException;


public class MockController extends ApplicationController {

    @Override
    protected Response get(Request request) throws IOException {
        return new Response.Builder(200).build();
    }

    @Override
    protected Response post(Request request) {
        return super.post(request);
    }

}
