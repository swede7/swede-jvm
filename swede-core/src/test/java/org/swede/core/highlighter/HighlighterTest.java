package org.swede.core.highlighter;

import com.sun.tools.javac.Main;
import org.eclipse.lsp4j.SemanticTokens;
import org.junit.jupiter.api.Test;
import org.swede.core.lsp.mapper.TokenMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

class HighlighterTest {

    @Test
    public void testHighlighter() throws IOException {
        String code = readFromFile("code/good-case.speca");

        Highlighter highlighter = new Highlighter(code);

        List<Token> tokens = highlighter.highlight();


        SemanticTokens semanticTokens = TokenMapper.mapTokens(tokens);
        System.out.println(semanticTokens);
//        assertEquals(5, tokens.size());
//
//        assertEquals(0, tokens.get(0).getStartPosition().getLine());
//        assertEquals(4, tokens.get(0).getStartPosition().getLineCharIndex());
//        assertEquals(4, tokens.get(0).getStartPosition().getLine());
    }

    private static String readFromFile(String path) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(path);
        assert is != null;
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();
        return text;
    }
}
