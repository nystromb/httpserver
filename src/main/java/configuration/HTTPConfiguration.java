package configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HTTPConfiguration implements ServerConfiguration {
    private Path publicDirectory = Paths.get(System.getProperty("user.dir"));
    private int port = 5000;

    public HTTPConfiguration() { }

    public HTTPConfiguration(String[] args) {
        for (int option = 0; option < args.length; option++) {

            switch (args[option]) {
                case "-p":
                    setPort(Integer.parseInt(args[++option]));
                    break;
                case "-d":
                    setPublicDirectory(args[++option]);
                    break;
            }
        }
    }

    private void setPort(int port) {
        this.port = port;
    }

    public void setPublicDirectory(String publicDirectory) {
        this.publicDirectory = Paths.get(publicDirectory);
    }

    public int getPort() {
        return port;
    }

    public Path getPublicDirectory() {
        return publicDirectory;
    }
}
