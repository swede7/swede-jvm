package org.swede.core.highlighter;

import org.swede.core.ast.*;
import org.swede.core.parser.Parser;

import java.util.ArrayList;
import java.util.List;

public class Highlighter {
    private final String code;


    public Highlighter(String code) {
        this.code = code;
    }

    public List<Token> highlight() {
        Parser parser = new Parser(code);

        DocumentNode documentNode = parser.parse();

        List<Token> tokens = new ArrayList<>();
        Utils.visitTree(documentNode, node -> processNode(node, tokens));
        return tokens;
    }

    private void processNode(AbstractNode node, List<Token> tokens) {
        Token token = new Token();
        token.setStartPosition(node.getStartPosition());
        token.setEndPosition(node.getEndPosition());

        if (node instanceof TagNode) {
            token.setType(TokenType.TAG);
            tokens.add(token);
        }
        if (node instanceof CommentNode) {
            token.setType(TokenType.COMMENT);
            tokens.add(token);
        }
        if (node instanceof StepNode) {
            token.setType(TokenType.STEP);
            tokens.add(token);
        }
        if (node instanceof KeywordNode) {
            token.setType(TokenType.KEYWORD);
            tokens.add(token);
        }
    }
}
