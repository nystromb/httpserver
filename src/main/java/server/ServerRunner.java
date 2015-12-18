package server;

import builders.Request;
import builders.RequestParser;
import builders.RequestReader;
import builders.Response;
import router.Route;
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
    private static final Logger logger = Logger.getLogger( "http.log" );
    private Socket client;
    private Response response;

    public ServerRunner(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStream output = client.getOutputStream()
        ){
            String rawRequest = RequestReader.read(input);
            Request request = RequestParser.process(rawRequest);

            Route route = Router.getRoute(request.getPath());
            if (route != null) {
                response = route.handle(request);
            } else {
                response = new Response.Builder(404, "Not Found").build();
            }
            logger.log(Level.INFO, rawRequest);

            output.write(response.toByteArray());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Something went wrong fulfilling builders");
        } catch (URISyntaxException e) {
            logger.log(Level.SEVERE, "Bad Request: URI Syntax Exception");
        }
    }
}
