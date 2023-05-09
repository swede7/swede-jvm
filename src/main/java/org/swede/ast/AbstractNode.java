package org.swede.ast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractNode {
    private List<AbstractNode> children = new ArrayList<>();

    public <T extends AbstractNode> List<T> getChildrenByClass(Class<T> classSelector) {
        return children.stream()
                .filter(abstractNode -> abstractNode.getClass().isAssignableFrom(classSelector))
                .map(abstractNode -> (T) abstractNode)
                .collect(Collectors.toList());
    }

    public <T extends AbstractNode> Optional<T> getChildByClass(Class<T> classSelector) {
        var filteredChildren = getChildrenByClass(classSelector);

        if (filteredChildren.size() == 0) {
            return Optional.empty();
        }
        if (filteredChildren.size() == 1) {
            return Optional.of(filteredChildren.get(0));

        }
        throw new RuntimeException("oops");
    }

    public void addChild(AbstractNode child) {
        children.add(child);
    }
}
