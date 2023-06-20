package org.swede.core.highlighter;

import org.swede.core.ast.AbstractNode;
import org.swede.core.ast.DocumentNode;
import org.swede.core.ast.TagNode;
import org.swede.core.ast.Utils;
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
    }
}
