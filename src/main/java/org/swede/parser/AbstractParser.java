package org.swede.parser;

import org.swede.ast.AbstractNode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractParser {


    private final String code;
    protected int pos;

    protected final List<AbstractNode> nodes = new ArrayList<>();

    public AbstractParser(String code) {
        this.code = code;
        this.pos = 0;
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

    abstract void parse();
}
