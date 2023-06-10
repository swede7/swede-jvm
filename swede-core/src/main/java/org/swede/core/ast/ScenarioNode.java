package org.swede.core.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class ScenarioNode extends AbstractNode {

    public String getDescription() {
        TextNode textNode = getChildByClass(TextNode.class).orElseThrow();
        return textNode.getText().trim();
    }

    public List<String> getTags() {
        var tags = getChildrenByClass(TagNode.class);
        return tags.stream().map(TagNode::getName).collect(Collectors.toList());
    }

    public List<String> getSteps() {
        var steps = getChildrenByClass(StepNode.class);
        return steps.stream().map(StepNode::getText).map(String::trim).collect(Collectors.toList());
    }
}
