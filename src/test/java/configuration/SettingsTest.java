package configuration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SettingsTest {
    @Before
    public void setUp() throws IOException {
    }

    @After
    public void cleanUp() throws IOException {
        Settings.PUBLIC_DIR = System.getProperty("user.dir") + "/public/";
        Settings.PORT = 5000;
    }

    @Test
    public void testParseArgs() {
        Settings.parse(new String[]{"-p", "9009", "-d", "/"});

        assertTrue(Settings.PUBLIC_DIR == "/");
        assertTrue(Settings.PORT == 9009);
    }

	@Test
	public void testDefaultPortAndPublicDir() {
		assertEquals(5000, Settings.PORT);
        assertEquals(System.getProperty("user.dir") + "/public/", Settings.PUBLIC_DIR);
    }

    @Test
    public void testCreatesLogsDirectory() {
        Settings.setUpLogger();

        assertTrue(Files.exists(new File(System.getProperty("user.dir"), "/logs/").toPath()));
    }
}