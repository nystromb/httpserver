package server;

import configuration.HTTPConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HttpServerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    Logger logger = Logger.getLogger("server.logs");
    HttpServer server;

    @Before
    public void setUp() throws IOException {
        server = new HttpServer(config, new HashMap<>(), logger);
    }

    @After
    public void tearDown() {
        server.shutDown();
    }

    @Test
    public void testServerIsListeningWhenStarted() throws IOException {
        assertTrue(server.isListening());
    }

    @Test
    public void testServerIsNotListeningWhenShutDown() throws IOException {
        server.shutDown();

        assertFalse(server.isListening());
    }
}


