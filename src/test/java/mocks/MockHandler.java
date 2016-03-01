package mocks;

import builders.Request;
import builders.Response;
import handlers.ApplicationHandler;

import java.nio.file.Path;


public class MockHandler extends ApplicationHandler {
    Path publicDirectory;

    public MockHandler() {}

    public MockHandler(Path publicDirectory) {
        this.publicDirectory = publicDirectory;
    }

    @Override
    protected Response get(Request request) {
        return new Response.Builder(200).build();
    }

    @Override
    protected Response post(Request request) {
        return super.post(request);
    }

}
