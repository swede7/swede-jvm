package org.swede.core.highlighter;

import org.swede.core.ast.Node;
import org.swede.core.ast.Utils;
import org.swede.core.common.Position;
import org.swede.core.lexer.Lexer;
import org.swede.core.parser.Parser;
import org.swede.core.parser.ParserResult;

import java.util.ArrayList;
import java.util.List;

public final class Highlighter {

    private Highlighter() {

    }

    public static List<Token> highlight(String code) {
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer.scan());

        ParserResult parserResult = parser.parse();
        var rootNode = parserResult.getRootNode();

        List<Token> tokens = new ArrayList<>();
        Utils.visitTree(rootNode, node -> processNode(node, tokens));
        return tokens;
    }

    private static void processNode(Node node, List<Token> tokens) {
        Token token = new Token();
        token.setStartPosition(node.getStartPosition());
        token.setEndPosition(node.getEndPosition());

        if (node.getType() == Node.NodeType.TAG) {
            token.setType(TokenType.TAG);
            tokens.add(token);
        }
        if (node.getType() == Node.NodeType.COMMENT) {
            token.setType(TokenType.COMMENT);
            tokens.add(token);
        }
        if (node.getType() == Node.NodeType.STEP) {
            token.setType(TokenType.STEP);
            tokens.add(token);
        }
//        if (node.getType() == Node.NodeType.FEATURE) {
//            token.setType(TokenType.KEYWORD);
//            // 8 = 'Feature:' string size
//            var endPosition = new Position(node.getStartPosition().offset() + 8, node.getStartPosition().line(), node.getStartPosition().column() + 8);
//            token.setEndPosition(endPosition);
//            tokens.add(token);
//        }
//
//        if (node.getType() == Node.NodeType.SCENARIO) {
//            token.setType(TokenType.KEYWORD);
//            // 8 = 'Feature:' string size
//            var endPosition = new Position(node.getStartPosition().offset() + 9, node.getStartPosition().line(), node.getStartPosition().column() + 9);
//            token.setEndPosition(endPosition);
//            tokens.add(token);
//        }
    }
}
