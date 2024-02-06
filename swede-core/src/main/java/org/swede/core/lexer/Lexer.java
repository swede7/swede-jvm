package org.swede.core.lexer;

import org.swede.core.common.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Lexer {
    private static final String FEATURE_WORD = "Feature:";
    private static final String SCENARIO_WORD = "Scenario:";

    private final String source;

    private int offset;
    private int line;
    private int column;
    private final List<Token> tokens = new ArrayList<>();

    private final List<Supplier<Boolean>> scanFunctions = List.of(this::scanNl, this::scanAt, this::scanDash, this::scanHash, this::scanSpace, this::scanWord);


    public Lexer(String source) {
        this.source = source;
    }

    public List<Token> scan() {
        while (!isAtEnd()) {
            scanNextToken();
        }
        return tokens;
    }


    private void scanNextToken() {
        for (var scanFunction : scanFunctions) {
            if (scanFunction.get()) {
                System.out.println(tokens.get(tokens.size() - 1));
                return;
            }
        }

        if (!isAtEnd()) {
            throw new IllegalStateException();
        }
    }

    private boolean scanWord() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();
        if (match(SCENARIO_WORD)) {
            addToken(Token.TokenType.SCENARIO_WORD, startPosition, new Position(offset - 1, line, column - 1), SCENARIO_WORD);
            return true;
        }

        if (match(FEATURE_WORD)) {
            addToken(Token.TokenType.FEATURE_WORD, startPosition, new Position(offset - 1, line, column - 1), FEATURE_WORD);
            return true;
        }

        StringBuilder stringBuilder = new StringBuilder();

        while (!isAtEnd()) {
            var currentChar = peek();
            if (Character.isSpaceChar(currentChar) || Character.isWhitespace(currentChar)) {
                break;
            }
            stringBuilder.append(currentChar);
            advance();
        }

        addToken(Token.TokenType.WORD, startPosition, new Position(offset - 1, line, column - 1), stringBuilder.toString());
        return true;
    }


    private boolean scanNl() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (match("\r\n")) {
            addToken(Token.TokenType.NL, startPosition, new Position(offset - 1, line, column - 1), "\r\n");
            line++;
            column = 0;
            return true;
        }

        if (match("\n")) {
            addToken(Token.TokenType.NL, startPosition, new Position(offset - 1, line, column - 1), "\n");
            line++;
            column = 0;
            return true;
        }

        return false;
    }

    private boolean scanAt() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (!match('@')) return false;

        addToken(Token.TokenType.AT_CHR, startPosition, "@");
        return true;
    }

    private boolean scanDash() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (!match('-')) return false;

        addToken(Token.TokenType.DASH_CHR, startPosition, "@");
        return true;
    }

    private boolean scanHash() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (!match('#')) return false;

        addToken(Token.TokenType.HASH_CHR, startPosition, "@");
        return true;
    }


    private boolean scanSpace() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        StringBuilder sb = new StringBuilder();

        while (!isAtEnd() && (peek() == ' ' || peek() == '\t')) {
            sb.append(peek());
            advance();
        }

        if (sb.isEmpty()) return false;

        addToken(Token.TokenType.SPACE, startPosition, new Position(offset - 1, line, column - 1), sb.toString());
        return true;
    }


    private Position getPosition() {
        return new Position(offset, line, column);
    }

    private void advance() {
        offset++;
        column++;
    }

    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (peek() != expected) {
            return false;
        }
        advance();
        return true;
    }

    private boolean match(String expected) {
        if (isAtEnd()) return false;

        if (expected.length() > charsLeft()) return false;

        var startPosition = getPosition();

        for (int i = 0; i < expected.length(); i++) {
            if (expected.charAt(i) != peek()) {
                //rollback
                setPosition(startPosition);
                return false;
            }
            advance();
        }
        return true;
    }

    public void setPosition(Position position) {
        this.offset = position.offset();
        this.line = position.line();
        this.column = position.column();
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

    private int charsLeft() {
        return source.length() - offset;
    }

}
