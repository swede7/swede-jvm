package org.swede.core.parser;

import org.swede.core.ast.Node;
import org.swede.core.common.Position;
import org.swede.core.lexer.Lexeme;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParser {

    private List<Lexeme> lexemes;
    private List<Node> nodes = new ArrayList<>();

    private List<ParserError> errors = new ArrayList<>();
    private int pos;


    protected AbstractParser(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    protected Lexeme peekLexeme() {
        return lexemes.get(pos);
    }


    protected List<Node> getNodes() {
        return nodes;
    }

    protected List<ParserError> getErrors() {
        return errors;
    }

    protected void advance() {
        advance(1);
    }

    protected void advance(int count) {
        pos += count;
    }

    protected boolean isEof() {
        return pos >= lexemes.size();
    }

    protected Lexeme lookup(int count) {
        return lexemes.get(pos + count);
    }

    protected int lexemesLeft() {
        return lexemes.size() - pos;
    }


    protected void addNode(Node node) {
        nodes.add(node);
    }

    protected Lexeme getPreviousLexeme() {
        return lexemes.get(pos - 1);
    }

    protected void addError(Position startPosition, Position endPosition, String message) {
        errors.add(new ParserError(startPosition, endPosition, message));
    }
}
