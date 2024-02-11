//package org.swede.core.highlighter;
//
//import org.swede.core.ast.*;
//import org.swede.core.lexer.Lexer;
//import org.swede.core.parser.Parser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public final class Highlighter {
//
//    private Highlighter() {
//
//    }
//
//    public static List<Token> highlight(String code) {
//        Lexer lexer = new Lexer(code);
//        Parser parser = new Parser(lexer.scan());
//
//        DocumentNode documentNode = parser.parse();
//
//        List<Token> tokens = new ArrayList<>();
//        Utils.visitTree(documentNode, node -> processNode(node, tokens));
//        return tokens;
//    }
//
//    private static void processNode(Node node, List<Token> tokens) {
//        Token token = new Token();
//        token.setStartPosition(node.getStartPosition());
//        token.setEndPosition(node.getEndPosition());
//
//        if (node instanceof TagNode) {
//            token.setType(TokenType.TAG);
//            tokens.add(token);
//        }
//        if (node instanceof CommentNode) {
//            token.setType(TokenType.COMMENT);
//            tokens.add(token);
//        }
//        if (node instanceof StepNode) {
//            token.setType(TokenType.STEP);
//            tokens.add(token);
//        }
//        if (node instanceof KeywordNode) {
//            token.setType(TokenType.KEYWORD);
//            tokens.add(token);
//        }
//    }
//}
