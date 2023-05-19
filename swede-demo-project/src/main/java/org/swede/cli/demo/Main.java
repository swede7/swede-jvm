package org.swede.cli.demo;

import org.swede.cli.interpreter.Interpreter;
import org.swede.cli.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        String code = readFromFile();

        Parser parser = new Parser(code);
        var documentNode = parser.parse();

        Interpreter interpreter = new Interpreter();
        interpreter.registerActionClass(ExampleStepImpl.class);
        interpreter.execute(documentNode);
    }


    private static String readFromFile() throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream("example.speca");
        //noinspection DataFlowIssue
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();
        return text;
    }
}
