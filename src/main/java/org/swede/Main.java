package org.swede;

import org.swede.parser.Parser;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        String code = readFromFile();

        Parser parser = new Parser(code);

        parser.parse();
    }


    private static String readFromFile() throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("example.feature");
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();
        return text;
    }
}
