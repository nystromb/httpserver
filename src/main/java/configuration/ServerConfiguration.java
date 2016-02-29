package configuration;

import java.nio.file.Path;

public interface ServerConfiguration {
    int getPort();
    Path getPublicDirectory();
}
