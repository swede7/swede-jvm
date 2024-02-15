package org.swede.core.formatter;

import org.swede.core.ast.Node;
import org.swede.core.lexer.Lexer;
import org.swede.core.parser.Parser;

public final class Formatter {
    private static final String NL = "\n";


    private Formatter() {
    }

    public static String format(String code) {
        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer.scan());
        var parserResult = parser.parse();

        // not format it if some errors found
        if (!parserResult.getErrors().isEmpty()) {
            return code;
        }

        var rootNode = parserResult.getRootNode();

        var builder = new StringBuilder();
        for (var node : rootNode.getChildren()) {
            formatNode(node, builder);
        }


        return builder.toString();
    }

    private static void formatNode(Node node, StringBuilder builder) {
        switch (node.getType()) {
            case FEATURE -> formatFeature(node, builder);
            case SCENARIO -> formatScenario(node, builder);
            case TAG -> formatTag(node, builder);
            case COMMENT -> formatComment(node, builder);
            case STEP -> formatStep(node, builder);
            default -> throw new RuntimeException();
        }
    }

    private static void formatStep(Node node, StringBuilder builder) {
        builder.append("- ");
        builder.append(node.getValue().trim());
        builder.append(NL);
    }

    private static void formatComment(Node node, StringBuilder builder) {
        builder.append("#");
        builder.append(node.getValue().trim());
        builder.append(NL).append(NL);
    }

    private static void formatTag(Node node, StringBuilder builder) {
        builder.append("@").append(node.getValue()).append(" ");
    }

    private static void formatScenario(Node node, StringBuilder builder) {
        var tags = node.getChildrenByType(Node.NodeType.TAG);
        for (var tag : tags) {
            formatTag(tag, builder);
        }

        builder.append(NL);
        builder.append("Scenario: ");
        builder.append(node.getValue().trim());
        builder.append(NL);

        var steps = node.getChildrenByType(Node.NodeType.STEP);
        for (var step : steps) {
            formatStep(step, builder);
        }

        builder.append(NL);
    }


    private static void formatFeature(Node node, StringBuilder builder) {
        var tags = node.getChildrenByType(Node.NodeType.TAG);
        for (var tag : tags) {
            formatTag(tag, builder);
        }

        builder.append(NL);
        builder.append("Feature: ");
        builder.append(node.getValue().trim());
        builder.append(NL).append(NL);
    }

}
