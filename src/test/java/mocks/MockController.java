package mocks;

import builders.Request;
import builders.Response;
import handlers.ApplicationController;

import java.io.IOException;
import java.nio.file.Path;


public class MockController extends ApplicationController {
    Path publicDirectory;

    public MockController() {}

    public MockController(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) throws IOException {
        return new Response.Builder(200).build();
    }

    @Override
    protected Response post(Request request) {
        return super.post(request);
    }

}
