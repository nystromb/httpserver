package helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SpecHelper {

    public SpecHelper() { }

    public Path createTempFile(Path dir, String fileName, String ext) throws IOException {
        if (ext == null)
            ext = ".txt";
        Path file = Files.createTempFile(dir, fileName, ext);
        file.toFile().deleteOnExit();
        return file;
    }

    public void writeTo(Path filePath, String contents) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath.toFile()));
        writer.write(contents);
        writer.close();
    }
}
