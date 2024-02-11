package org.swede.core.lexer;

import org.swede.core.common.Position;

import java.util.List;
import java.util.function.Supplier;

public class Lexer extends AbstractLexer {
    private static final String FEATURE_WORD = "Feature:";
    private static final String SCENARIO_WORD = "Scenario:";

    private final List<Supplier<Boolean>> scanFunctions = List.of(this::scanNl, this::scanAt, this::scanDash, this::scanHash, this::scanSpace, this::scanWord);


    public Lexer(String source) {
        super(source);
    }

    public List<Lexeme> scan() {
        while (!isAtEnd()) {
            scanNextToken();
        }
        return getLexemes();
    }


    private void scanNextToken() {
        for (var scanFunction : scanFunctions) {
            if (scanFunction.get()) {
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
            addToken(Lexeme.LexemeType.SCENARIO_WORD, startPosition, new Position(getOffset() - 1, getLine(), getColumn() - 1), SCENARIO_WORD);
            return true;
        }

        if (match(FEATURE_WORD)) {
            addToken(Lexeme.LexemeType.FEATURE_WORD, startPosition, new Position(getOffset() - 1, getLine(), getColumn() - 1), FEATURE_WORD);
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

        addToken(Lexeme.LexemeType.WORD, startPosition, new Position(getOffset() - 1, getLine(), getColumn() - 1), stringBuilder.toString());
        return true;
    }


    private boolean scanNl() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (match("\r\n")) {
            addToken(Lexeme.LexemeType.NL, startPosition, new Position(getOffset() - 1, getLine(), getColumn() - 1), "\r\n");
            setLine(getLine() + 1);
            setColumn(0);
            return true;
        }

        if (match("\n")) {
            addToken(Lexeme.LexemeType.NL, startPosition, new Position(getColumn() - 1, getLine(), getColumn() - 1), "\n");
            setLine(getLine() + 1);
            setColumn(0);
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

        addToken(Lexeme.LexemeType.AT_CHR, startPosition, "@");
        return true;
    }

    private boolean scanDash() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (!match('-')) return false;

        addToken(Lexeme.LexemeType.DASH_CHR, startPosition, "-");
        return true;
    }

    private boolean scanHash() {
        if (isAtEnd()) {
            return false;
        }

        var startPosition = getPosition();

        if (!match('#')) return false;

        addToken(Lexeme.LexemeType.HASH_CHR, startPosition, "#");
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


        addToken(Lexeme.LexemeType.SPACE, startPosition, new Position(getOffset() - 1, getLine(), getColumn() - 1), sb.toString());
        return true;
    }


}
