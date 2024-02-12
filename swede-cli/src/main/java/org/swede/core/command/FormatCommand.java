package org.swede.core.command;

import org.swede.core.formatter.Formatter;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Command(name = "format")
public class FormatCommand implements Runnable {

    @Parameters(index = "0", description = "The path of file that needs to be formatted")
    private Path path;

    @Override
    public void run() {
        String code = readFormFile();
        String formattedCode = Formatter.format(code);
        writeToFile(formattedCode);
    }

    private String readFormFile() {
        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeToFile(String formattedCode) {
        try {
            Files.writeString(path, formattedCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
