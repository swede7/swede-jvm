package org.swede.core.formatter;

import org.swede.core.ast.*;
import org.swede.core.parser.Parser;

import java.util.List;

public class Formatter {
    private static final String END_OF_LINE = "\n";
    private final String code;


    public Formatter(String code) {
        this.code = code;
    }

    public String format() {
        Parser parser = new Parser(code);
        var rootNode = parser.parse();

        var builder = new StringBuilder();
        formatDocumentNode(rootNode, builder);

        return builder.toString();
    }

    private void formatDocumentNode(DocumentNode node, StringBuilder builder) {
        List<TagNode> tags = node.getChildrenByClass(TagNode.class);
        if (tags.size() > 0) {
            formatTags(tags, builder);
        }

        String description = node.getDescription();
        builder.append("Feature: ").append(description.trim()).append(END_OF_LINE).append(END_OF_LINE);

        for (var childNode : node.getChildren()) {
            if (childNode instanceof CommentNode) {
                formatCommentNode((CommentNode) childNode, builder);
            }
            if (childNode instanceof ScenarioNode) {
                formatScenarioNode((ScenarioNode) childNode, builder);
            }
        }
    }

    private void formatCommentNode(CommentNode node, StringBuilder builder) {
        String comment = node.getComment();
        builder.append("# ").append(comment.trim()).append(END_OF_LINE).append(END_OF_LINE);
    }

    private void formatScenarioNode(ScenarioNode node, StringBuilder builder) {
        List<TagNode> tags = node.getChildrenByClass(TagNode.class);
        if (tags.size() > 0) {
            formatTags(tags, builder);
        }

        String description = node.getDescription();
        builder.append("Scenario: ").append(description).append(END_OF_LINE);

        List<StepNode> stepNodes = node.getChildrenByClass(StepNode.class);
        for (var stepNode : stepNodes) {
            formatStepNode(stepNode, builder);
        }

        builder.append(END_OF_LINE);
    }

    private void formatStepNode(StepNode node, StringBuilder builder) {
        builder.append("- ").append(node.getText().trim()).append(END_OF_LINE);
    }

    private void formatTags(List<TagNode> tags, StringBuilder builder) {
        assert tags.size() > 0;

        for (int i = 0; i < tags.size() - 1; i++) {
            var tag = tags.get(i);
            builder.append("@").append(tag.getName()).append(" ");
        }
        builder.append("@").append(tags.get(tags.size() - 1).getName());
        builder.append(END_OF_LINE);
    }


}
