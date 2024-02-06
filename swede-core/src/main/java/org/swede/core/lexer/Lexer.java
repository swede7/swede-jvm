package org.swede.core.lexer;

import org.swede.core.common.Position;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String source;

    private int offset;
    private int line;
    private int column;
    private final List<Token> tokens = new ArrayList<>();

    public Lexer(String source) {
        this.source = source;
    }

    private List<Lexer> scan() {
        while (isAtEnd()) {
            scanNextToken();
        }
    }

    private void scanNextToken() {
        char c = peek();

        switch (c) {
            case ' ', '\t' -> scanSpace();
            case '-' -> scanDash();
            case '@' -> scanAt();
            case '\n' -> scanNl();
            case test(c) -> scanDash();
        }
    }

    private boolean test(char c) {
        return true;
    }

    private void scanNl() {
        var startPosition = getPosition();

        if (match(source))
    }

    private boolean scanAt() {
        var startPosition = getPosition();

        if (!match('@')) return false;

        addToken(Token.TokenType.AT_CHR, startPosition, "@");
        advance();
        return true;
    }

    private boolean scanDash() {
        var startPosition = getPosition();

        if (!match('-')) return false;

        addToken(Token.TokenType.AT_CHR, startPosition, "@");
        advance();
        return true;
    }

    private void scanSpace() {
        var startPosition = getPosition();

        StringBuilder sb = new StringBuilder();

        while (match(' ') || match('\t')) {
            sb.append(advance());
        }
        
        addToken(Token.TokenType.SPACE, startPosition, getPosition(), sb.toString());
    }

    private boolean isSpace() {
        return
    }

    private Position getPosition() {
        return new Position(offset, line, column);
    }

    private char advance() {
        return source.charAt(offset++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        return source.charAt(offset) == expected;
    }

    private char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(offset);
    }

    private void addToken(Token.TokenType tokenType, Position startPosition, Position endPosition, String value) {
        tokens.add(new Token(tokenType, startPosition, endPosition, value));
    }

    private void addToken(Token.TokenType tokenType, Position position, String value) {
        tokens.add(new Token(tokenType, position, position, value));
    }


    private boolean isAtEnd() {
        return offset >= source.length();
    }


}
