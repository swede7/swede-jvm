package org.swede.core.lexer;

import org.swede.core.common.Position;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLexer {

    private final String source;

    private int offset;
    private int line;
    private int column;

    private final List<Lexeme> lexemes = new ArrayList<>();


    public AbstractLexer(String source) {
        this.source = source;
    }

    protected boolean isAtEnd() {
        return offset >= source.length();
    }

    protected void advance() {
        offset++;
        column++;
    }

    protected List<Lexeme> getLexemes() {
        return lexemes;
    }

    protected Position getPosition() {
        return new Position(offset, line, column);
    }

    protected int charsLeft() {
        return source.length() - offset;
    }

    protected void setPosition(Position position) {
        this.offset = position.offset();
        this.line = position.line();
        this.column = position.column();
    }

    protected char peek() {
        if (isAtEnd()) return '\0';
        return source.charAt(offset);
    }

    protected boolean match(char expected) {
        if (isAtEnd()) return false;
        if (peek() != expected) {
            return false;
        }
        advance();
        return true;
    }

    protected boolean match(String expected) {
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

    protected void addToken(Lexeme.LexemeType lexemeType, Position startPosition, Position endPosition, String value) {
        lexemes.add(new Lexeme(lexemeType, startPosition, endPosition, value));
    }

    protected void addToken(Lexeme.LexemeType lexemeType, Position position, String value) {
        lexemes.add(new Lexeme(lexemeType, position, position, value));
    }


    protected int getOffset() {
        return offset;
    }

    protected void setOffset(int offset) {
        this.offset = offset;
    }

    protected int getLine() {
        return line;
    }

    protected void setLine(int line) {
        this.line = line;
    }

    protected int getColumn() {
        return column;
    }

    protected void setColumn(int column) {
        this.column = column;
    }
}
