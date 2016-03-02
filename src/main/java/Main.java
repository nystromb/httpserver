import configuration.HTTPConfiguration;
import server.HttpServer;

import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            HttpServer http = new HttpServer(new HTTPConfiguration(), new HashMap<>());
            http.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
