package org.swede.core.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class DocumentNode extends AbstractNode {

    public String getDescription() {
        return getChildByClass(TextNode.class).get().getText();
    }

    public List<String> getTags() {
        var tags = getChildrenByClass(TagNode.class);
        return tags.stream()
                .map(TagNode::getName)
                .collect(Collectors.toList());
    }

    public List<ScenarioNode> getScenariosNodes() {
        return getChildrenByClass(ScenarioNode.class);
    }

}
