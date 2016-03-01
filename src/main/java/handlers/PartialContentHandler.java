package handlers;

import builders.Request;
import builders.Response;

public class PartialContentHandler extends ApplicationHandler {
    private Response.Builder response = new Response.Builder(206);
    private String fileContents;

    public PartialContentHandler(String fileContents) {
        this.fileContents = fileContents;
    }

    @Override
    protected Response get(Request request) {
        String ranges = request.getHeader("Range").split("=")[1];
        String[] range = ranges.split("-");

        if (ranges.startsWith("-")) {
            response.setBody(fileContents.substring(fileContents.length() - Integer.parseInt(range[1])));
        } else if (ranges.endsWith("-")) {
            response.setBody(fileContents.substring(Integer.parseInt(range[0])));
        } else {
            response.setBody(fileContents.substring(Integer.parseInt(range[0]), (Integer.parseInt(range[1]) + 1)));
        }

        return response.build();
    }
}
