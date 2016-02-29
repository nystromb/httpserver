package server;

import configuration.ServerConfiguration;
import router.Route;
import router.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private ServerSocket server;
    private Router router;
    private Socket client;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    public HttpServer(ServerConfiguration config, HashMap<String, Route> routes) throws IOException {
        server = new ServerSocket(config.getPort());
        router = new Router(config.getPublicDirectory(), routes);
    }

    public void start() throws IOException {
        while (isListening()) {
            client = server.accept();
            executorService.execute(new ServerRunner(client, router));
        }
    }

    public boolean isListening() {
        return !server.isClosed();
    }

    public void shutDown() {
        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

