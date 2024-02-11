package org.swede.core.ast;

import java.util.function.Consumer;

public final class Utils {

    private Utils() {
    }

    public static void visitTree(Node node, Consumer<Node> consumer) {
        for (Node child : node.getChildren()) {
            visitTree(child, consumer);
        }
        consumer.accept(node);
    }

}
