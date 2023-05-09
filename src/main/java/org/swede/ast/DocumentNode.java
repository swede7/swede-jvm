package org.swede.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentNode extends AbstractNode {

    private String getDescription() {
        return getChildByClass(TextNode.class).get().getText();
    }

    private List<String> getTags() {
        var tags = getChildrenByClass(TagNode.class);
        return tags.stream()
                .map(TagNode::getName)
                .collect(Collectors.toList());
    }

    private List<ScenarioNode> getScenarios() {
        return getChildrenByClass(ScenarioNode.class);
    }

}
