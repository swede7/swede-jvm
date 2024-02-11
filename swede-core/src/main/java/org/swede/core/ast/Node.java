package org.swede.core.ast;

import lombok.Getter;
import lombok.Setter;
import org.swede.core.common.Position;

import java.util.LinkedList;
import java.util.List;


@Getter
@Setter
public class Node {
    private NodeType type;
    private List<Node> children = new LinkedList<>();
    private Position startPosition;
    private Position endPosition;
    private String value;


    public Node(NodeType type, Position startPosition, Position endPosition, String value) {
        this.type = type;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.value = value;
    }

    public Node(NodeType type, Position position, String value) {
        this(type, position, position, value);
    }


    public enum NodeType {
        UNEXPECTED,
        COMMENT,
        DOCUMENT,
        SCENARIO,
        FEATURE,
        STEP,
        TAG,
    }

    public void prependChild(Node node) {
        children.add(0, node);
    }

    public void appendChild(Node node) {
        children.add(node);
    }
}
