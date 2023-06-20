package org.swede.core.parser;

import org.swede.core.ast.AbstractNode;
import org.swede.core.common.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractParser {


    private final String code;
    private int textCharIndex;
    private int lineCharIndex;
    private int line;


    private final List<AbstractNode> nodes = new ArrayList<>();

    public AbstractParser(String code) {
        this.code = code;
        this.textCharIndex = 0;
        this.lineCharIndex = 0;
        this.line = 0;
    }

    public Position getPosition() {
        return new Position(textCharIndex, lineCharIndex, line);
    }

    public void setPosition(Position position) {
        this.textCharIndex = position.textCharIndex();
        this.lineCharIndex = position.lineCharIndex();
        this.line = position.line();
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
        return textCharIndex >= code.length();
    }

    protected char peek() {
        return code.charAt(textCharIndex);
    }

    protected char next() {
        char currChar = peek();
        if (currChar == '\n') {
            line++;
            lineCharIndex = -1;
        }
        lineCharIndex++;
        textCharIndex++;
        return currChar;
    }

    protected boolean parseMany(Supplier<Boolean> supplier) {
        Position startPos = getPosition();
        Position lastCorrectPos = getPosition();

        while (!isEOF() && supplier.get()) {
            lastCorrectPos = getPosition();
        }

        if (getPosition() == startPos) {
            return false;
        }

        setPosition(lastCorrectPos);
        return true;
    }

    protected boolean parseAndSkipChar(char c) {
        Position startPos = getPosition();
        if (next() != c) {
            //rollback
            setPosition(startPos);
            return false;
        }
        return true;
    }

    protected boolean parseAndSkipString(String s) {
        Position startPos = getPosition();

        int i = 0;
        while (!isEOF() && i < s.length() && s.charAt(i) == next()) {
            i++;
        }

        if (i == s.length()) {
            return true;
        }
        // else rollback
        setPosition(startPos);
        return false;
    }

    abstract AbstractNode parse();
}
