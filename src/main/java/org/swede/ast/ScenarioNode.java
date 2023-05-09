package org.swede.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioNode extends AbstractNode {

    private String getDescription() {
        return getChildByClass(TextNode.class).get().getText();
    }

    private List<String> getTags() {
        var tags = getChildrenByClass(TagNode.class);
        return tags.stream()
                .map(TagNode::getName)
                .collect(Collectors.toList());
    }

    private List<String> getSteps() {
        var steps = getChildrenByClass(StepNode.class);
        return steps.stream()
                .map(StepNode::getText)
                .collect(Collectors.toList());
    }
}
