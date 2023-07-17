package org.swede.core.lsp;

import org.eclipse.lsp4j.SemanticTokens;
import org.swede.core.highlighter.Token;
import org.swede.core.highlighter.TokenType;

import java.util.ArrayList;
import java.util.List;

public final class TokenMapper {

    private TokenMapper() {
    }

    public static SemanticTokens mapTokens(List<Token> tokens) {

        SemanticTokens semanticTokens = new SemanticTokens();
        List<Integer> data = new ArrayList<>();

        Token prevToken = null;

        for (Token token : tokens) {
            var startPosition = token.getStartPosition();
            var endPosition = token.getEndPosition();

            int deltaLine = prevToken == null ? startPosition.line() : startPosition.line() - prevToken.getStartPosition().line();
            data.add(deltaLine);

            int deltaStart;
            if (deltaLine == 0) {
                deltaStart = prevToken == null ? startPosition.lineCharIndex() : startPosition.lineCharIndex() - prevToken.getStartPosition().lineCharIndex();
            } else {
                deltaStart = startPosition.lineCharIndex();
            }
            data.add(deltaStart);


            int length = endPosition.textCharIndex() - startPosition.textCharIndex();
            data.add(length);

            int tokenType = mapTokenType(token.getType());
            data.add(tokenType);

            // todo
            int tokenModifiers = 0;
            data.add(tokenModifiers);

            prevToken = token;
        }

        semanticTokens.setData(data);
        return semanticTokens;
    }

    public static Integer mapTokenType(TokenType type) {
        return switch (type) {
            case TAG -> 0;
            case COMMENT -> 1;
            case STEP -> 2;
            case KEYWORD -> 3;
        };
    }


}
