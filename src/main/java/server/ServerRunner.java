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
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRunner implements Runnable {
    private Socket client;
    private Router router;
    private Logger logger;

    public ServerRunner(Socket client, Router router, Logger logger) {
        this.client = client;
        this.router = router;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream output = client.getOutputStream();

            String rawRequest = RequestReader.read(input);
            logger.log(Level.INFO, rawRequest);
            Request request = RequestParser.process(rawRequest);

            Response response = router.handle(request);
            logger.log(Level.INFO, new String(response.toByteArray()));
            output.write(response.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
