import configuration.HTTPConfiguration;
import router.Route;
import server.HttpServer;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        Logger logger = Logger.getLogger("server.logs");

        try {
            new HttpServer(new HTTPConfiguration(args), new HashMap<String, Route>(), logger).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
