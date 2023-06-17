package org.swede.core.lsp.mapper;

import org.eclipse.lsp4j.SemanticTokens;
import org.swede.core.highlighter.Token;
import org.swede.core.highlighter.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenMapper {
    public static SemanticTokens mapTokens(List<Token> tokens) {

        SemanticTokens semanticTokens = new SemanticTokens();
        List<Integer> data = new ArrayList<>();

        for (Token token : tokens) {
            int deltaLine = 0;
            int deltaStart = 0;
            int length;
            int tokenType = mapTokenType(token.getType());
            int tokenModifiers = 0;

            data.add(deltaLine);
            data.add(deltaStart);
            data.add(length);
            data.add(tokenType);
            data.add(tokenModifiers);
        }
    }

    public static Integer mapTokenType(TokenType type) {
        return switch (type) {
            case TAG -> 1;
        };
    }


}
