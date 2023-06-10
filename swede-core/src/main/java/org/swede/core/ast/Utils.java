package org.swede.core.ast;

public final class Utils {

    private Utils() {
    }

    public static void printTree(AbstractNode node) {
        printTree(node, 0);
    }

    private static void printTree(AbstractNode node, int padding) {
        for (int i = 0; i < padding; i++) {
            System.out.print(" ");
        }

        System.out.println(node.getClass().getSimpleName());
        for (AbstractNode child : node.getChildren()) {
            printTree(child, padding + 4);
        }

    }
}
