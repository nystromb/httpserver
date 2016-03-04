import configuration.HTTPConfiguration;
import server.HttpServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("server.logs");

        try {
            new HttpServer(new HTTPConfiguration(args), new HashMap<>(), logger).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
