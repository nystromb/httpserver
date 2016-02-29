package server;

import configuration.HTTPConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class HttpServerTest {
    HTTPConfiguration config = new HTTPConfiguration();
    HttpServer server;

    @Before
    public void setUp() throws IOException {
        server = new HttpServer(config, new HashMap<>());
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


