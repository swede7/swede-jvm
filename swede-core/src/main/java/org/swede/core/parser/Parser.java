package org.swede.core.parser;


import org.swede.core.ast.CommentNode;
import org.swede.core.ast.DocumentNode;
import org.swede.core.ast.StepNode;
import org.swede.core.lexer.Token;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(pos);
    }

    public void advance() {
        pos++;
    }

    private boolean isEof() {
        return pos >= tokens.size();
    }


    public DocumentNode parse() {
        // document: expression
        // expression: feature | scenario | comment | emptyline
        // feature: (tags)? FEATURE_WORD .* NL
        // comment: DASH .* NL
        // scenario: (tags)? SCENARIO_WORD NL step*
        // step: DASH .* NL
        // tags: (tag SPACE?) NL
        // tag: AT WORD
        // emptyline: SPACE? NL


        while (!isEof()) {
            switch (peek().getType()) {
                case HASH_CHR -> parseComment();
                case DASH_CHR -> parseStep();
            }
        }


        return null;
    }


    private CommentNode parseComment() {
        if (peek().getType() != Token.TokenType.HASH_CHR) {
            throw new IllegalStateException();
        }

        StringBuilder builder = new StringBuilder();

        var startPosition = peek().getStartPosition();
        var endPosition = startPosition;

        while (!isEof() && peek().getType() != Token.TokenType.NL) {
            builder.append(peek().getValue());
            endPosition = peek().getEndPosition();
            advance();
        }

        var node = new CommentNode();
        node.setStartPosition(startPosition);
        node.setEndPosition(endPosition);
        node.setComment(builder.toString());
        return node;
    }

    private StepNode parseStep() {
        if (peek().getType() != Token.TokenType.DASH_CHR) {
            throw new IllegalStateException();
        }

        StringBuilder builder = new StringBuilder();

        var startPosition = peek().getStartPosition();
        var endPosition = startPosition;

        while (!isEof() && peek().getType() != Token.TokenType.NL) {
            builder.append(peek().getValue());
            endPosition = peek().getEndPosition();
            advance();
        }

        var node = new StepNode();
        node.setStartPosition(startPosition);
        node.setEndPosition(endPosition);
        node.setText(builder.toString());
        return node;
    }

    boolean matchType(Token.TokenType type) {
        if (peek().getType() != type) {
            return false;
        }
        advance();
        return true;
    }

    private void parseTag() {
        if (matchType(Token.TokenType.AT_CHR)) {
            throw new IllegalStateException();
        }


    }


}
