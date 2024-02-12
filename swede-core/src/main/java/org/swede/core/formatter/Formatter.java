package org.swede.core.formatter;

public final class Formatter {
    private static final String END_OF_LINE = "\n";


    private Formatter() {
    }

    public static String format(String code) {
        //todo fix it
        return code;
//        Lexer lexer = new Lexer(code);
//        Parser parser = new Parser(lexer.scan());
//        var parserResult = parser.parse();
//
//        // not format it if some errors found
//        if (!parserResult.getErrors().isEmpty()) {
//            return code;
//        }
//
//        var rootNode = parserResult.getRootNode();
//
//        var builder = new StringBuilder();
//        formatDocumentNode(rootNode, builder);
    }

//    private static void formatDocumentNode(Node node, StringBuilder builder) {
//        List<TagNode> tags = node.getChildrenByClass(TagNode.class);
//        if (tags.size() > 0) {
//            formatTags(tags, builder);
//        }
//
//        String description = node.getDescription();
//        builder.append("Feature: ").append(description.trim()).append(END_OF_LINE).append(END_OF_LINE);
//
//        for (var childNode : node.getChildren()) {
//            if (childNode instanceof CommentNode) {
//                formatCommentNode((CommentNode) childNode, builder);
//            }
//            if (childNode instanceof ScenarioNode) {
//                formatScenarioNode((ScenarioNode) childNode, builder);
//            }
//        }
//    }
//
//    private static void formatCommentNode(CommentNode node, StringBuilder builder) {
//        String comment = node.getComment();
//        builder.append("# ").append(comment.trim()).append(END_OF_LINE).append(END_OF_LINE);
//    }
//
//    private static void formatScenarioNode(ScenarioNode node, StringBuilder builder) {
//        List<TagNode> tags = node.getChildrenByClass(TagNode.class);
//        if (tags.size() > 0) {
//            formatTags(tags, builder);
//        }
//
//        String description = node.getDescription();
//        builder.append("Scenario: ").append(description).append(END_OF_LINE);
//
//        List<StepNode> stepNodes = node.getChildrenByClass(StepNode.class);
//        for (var stepNode : stepNodes) {
//            formatStepNode(stepNode, builder);
//        }
//
//        builder.append(END_OF_LINE);
//    }
//
//    private static void formatStepNode(StepNode node, StringBuilder builder) {
//        builder.append("- ").append(node.getText().trim()).append(END_OF_LINE);
//    }
//
//    private static void formatTags(List<TagNode> tags, StringBuilder builder) {
//        assert tags.size() > 0;
//
//        for (int i = 0; i < tags.size() - 1; i++) {
//            var tag = tags.get(i);
//            builder.append("@").append(tag.getName()).append(" ");
//        }
//        builder.append("@").append(tags.get(tags.size() - 1).getName());
//        builder.append(END_OF_LINE);
//    }


}
