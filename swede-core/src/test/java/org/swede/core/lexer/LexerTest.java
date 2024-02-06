package org.swede.core.lexer;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

class LexerTest {
    @Test
    public void test() throws URISyntaxException, IOException {
        URL url = this.getClass().getClassLoader().getResource("lexer-test");
        File directory = new File(url.toURI());

        for (var file : directory.listFiles()) {
            String filename = file.getName();
            var code = Files.readString(file.toPath());
            executeTest(filename, code);
        }
    }

    private void executeTest(String filename, String code) {
        System.out.println("Running " + filename + " file...");

        var lexer = new Lexer(code);
        var tokens = lexer.scan();

        System.out.println(tokens);
    }

}
