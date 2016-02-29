package server;

import builders.Request;
import builders.RequestParser;
import builders.RequestReader;
import builders.Response;
import router.Router;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URISyntaxException;

public class ServerRunner implements Runnable {
    private Socket client;
    private Router router;

    public ServerRunner(Socket client, Router router) {
        this.client = client;
        this.router = router;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream output = client.getOutputStream();

            String rawRequest = RequestReader.read(input);
            Request request = RequestParser.process(rawRequest);

            Response response = router.handle(request);

            output.write(response.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
