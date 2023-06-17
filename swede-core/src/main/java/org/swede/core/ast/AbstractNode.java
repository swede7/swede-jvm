package org.swede.core.ast;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swede.core.common.Position;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractNode {
    private List<AbstractNode> children = new ArrayList<>();
    private Position startPosition;
    private Position endPosition;

    public <T extends AbstractNode> List<T> getChildrenByClass(Class<T> classSelector) {
        //noinspection unchecked
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

    public void addChildren(Collection<AbstractNode> child) {
        children.addAll(child);
    }
}
