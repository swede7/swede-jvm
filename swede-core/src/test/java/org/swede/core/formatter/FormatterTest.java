package org.swede.core.formatter;

import com.sun.tools.javac.Main;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FormatterTest {

    @Test
    void format() throws IOException {
        String code = readFromFile("code/good-case.speca");
        String expectedFormattedCode = readFromFile("code/formatted-good-case.speca");

        String formattedText = Formatter.format(code);

        assertEquals(expectedFormattedCode, formattedText);
    }

    private static String readFromFile(String path) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(path);
        assert is != null;
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();
        return text;
    }
}
