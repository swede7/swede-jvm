package org.swede.core.parser;

import org.swede.core.ast.AbstractNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractParser {


    private final String code;
    private int pos;

    private final List<AbstractNode> nodes = new ArrayList<>();

    public AbstractParser(String code) {
        this.code = code;
        this.pos = 0;
    }

    public int pos() {
        return pos;
    }

    public void pos(int newPos) {
        this.pos = newPos;
    }

    protected void addNode(AbstractNode node) {
        nodes.add(node);
    }

    protected AbstractNode getNode(int i) {
        return nodes.get(i);
    }

    protected List<AbstractNode> getNodes() {
        return nodes;
    }

    protected void removeNode(int index) {
        nodes.remove(index);
    }

    protected <T> T getNode(int i, Class<T> classSelector) {
        //noinspection unchecked
        return (T) nodes.get(i);
    }

    protected boolean isEOF() {
        return pos >= code.length();
    }

    protected char peek() {
        return code.charAt(pos);
    }

    protected char next() {
        char currChar = peek();
        pos++;
        return currChar;
    }

    protected boolean parseMany(Supplier<Boolean> supplier) {
        int startPos = pos;
        int lastCorrectPos = pos;

        while (!isEOF() && supplier.get()) {
            lastCorrectPos = pos;
        }

        if (pos == startPos) {
            return false;
        }

        pos = lastCorrectPos;
        return true;
    }

    protected boolean parseAndSkipChar(char c) {
        int startPos = pos;
        if (next() != c) {
            //rollback
            pos = startPos;
            return false;
        }
        return true;
    }

    protected boolean parseAndSkipString(String s) {
        int startPos = pos();

        int i = 0;
        while (!isEOF() && i < s.length() && s.charAt(i) == peek()) {
            pos++;
            i++;
        }

        if (i == s.length()) {
            return true;
        }
        // else rollback
        pos(startPos);
        return false;
    }

    abstract AbstractNode parse();
}
