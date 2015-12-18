package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private ServerSocket server;
    private Socket client;

    public HttpServer(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public void start() throws IOException {
        while(true) {
            client = server.accept();
            executorService.execute(new ServerRunner(client));
        }
    }
}

