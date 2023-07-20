package org.swede.core.ast;

import java.util.function.Consumer;

public final class Utils {

    private Utils() {
    }

    public static void visitTree(AbstractNode node, Consumer<AbstractNode> consumer) {
        for (AbstractNode child : node.getChildren()) {
            visitTree(child, consumer);
        }
        consumer.accept(node);
    }

}
