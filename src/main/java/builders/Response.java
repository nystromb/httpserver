package builders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private final String PROTOCOL = "HTTP/1.1";
    private HashMap<Integer, String> statusCodeMap = new ResponseCodes();

    private String statusLine = "";
    private HashMap<String, String> headers;
    private byte[] body = "".getBytes();

    public Response(Builder builder) {
        this.statusLine = PROTOCOL + " " + String.valueOf(builder.status) + " " + statusCodeMap.get(builder.status);
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String getStatusLine() {
        return statusLine;
    }

    public String getHeaderValue(String headerKey) {
        return headers.get(headerKey);
    }

    public boolean hasHeader(String headerKey) {
        return headers.containsKey(headerKey);
    }

    public byte[] getBody() {
        return body;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        buffer.write((statusLine + "\r\n").getBytes());

        for (Map.Entry<String, String> header : headers.entrySet()) {
            buffer.write((header.getKey() + ": " + header.getValue() + "\r\n").getBytes());
        }

        buffer.write("\r\n".getBytes());
        buffer.write(body);

        return buffer.toByteArray();
    }

    public static class Builder {
        private int status;
        private HashMap<String, String> headers = new HashMap<>();
        private byte[] body = "".getBytes();

        public Builder(int code, String body) {
            this.status = code;
            this.body = body.getBytes();
            setContentLength(body.getBytes());
        }

        public Builder(int code) {
            this.status = code;
            this.body = "".getBytes();
        }

        public Builder status(int code) {
            status = code;
            return this;
        }

        public Builder addHeader(String header, String value) {
            this.headers.put(header, value);
            return this;
        }

        public Builder setBody(byte[] contents) {
            body = contents;
            setContentLength(body);
            return this;
        }

        public Builder setBody(String contents) {
            body = contents.getBytes();
            setContentLength(contents.getBytes());
            return this;
        }

        private void setContentLength(byte[] body) {
            headers.put("Content-Length", String.valueOf(body.length));
        }

        public Response build() {
            return new Response(this);
        }
    }
}
