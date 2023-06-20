package org.swede.core.parser;


import org.swede.core.ast.*;
import org.swede.core.common.Position;

public class Parser extends AbstractParser {

    private static final char TAG_CHAR = '@';


    public Parser(String code) {
        super(code);
    }


    @Override
    public DocumentNode parse() {
        if (!parseDocument()) {
            throw new RuntimeException("parsing error");
        }

        if (getNodes().size() != 1) {
            throw new RuntimeException("parsing error");
        }
        return getNode(0, DocumentNode.class);
    }

    private boolean parseDocument() {
        Position startPos = getPosition();
        int startNodesCount = getNodes().size();
        DocumentNode documentNode = new DocumentNode();

        if (parseTags() && parseAndSkipString("Feature:") && parseText() && parseMany(this::parseExpression)) {
            int endNodesCount = getNodes().size();
            for (int i = startNodesCount; i < endNodesCount; i++) {
                documentNode.addChild(getNode(i));
            }

            for (int i = startNodesCount; i < endNodesCount; i++) {
                removeNode(getNodes().size() - 1);
            }
            addNode(documentNode);
            return true;
        }

        if (parseAndSkipString("Feature:") && parseText() && parseMany(this::parseExpression)) {
            int endNodesCount = getNodes().size();
            for (int i = startNodesCount; i < endNodesCount; i++) {
                documentNode.addChild(getNode(i));
            }

            for (int i = startNodesCount; i < endNodesCount; i++) {
                removeNode(getNodes().size() - 1);
            }
            addNode(documentNode);
            return true;
        }

        // rollback
        setPosition(startPos);
        return false;

    }

    // _ -> (CommentNode | ScenarioNode)*
    private boolean parseExpression() {
        Position startPos = getPosition();

        if (isEOF() || !parseMany(() -> parseComment() || parseScenario() || parseAndSkipWS())) {
            // else rollback
            setPosition(startPos);
            return false;
        }

        return true;
    }

    private boolean parseAndSkipWS() {
        Position startPos = getPosition();

        if (!isEOF() && parseMany(() -> parseAndSkipChar(' ') || parseAndSkipEL())) {
            return true;
        }
        // else rollback
        setPosition(startPos);
        return false;
    }

    // _ -> TagNode+
    private boolean parseTags() {
        Position startPos = getPosition();

        if (!parseTag()) {
            // else rollback
            setPosition(startPos);
            return false;
        }

        parseMany(() -> parseAndSkipChar(' ') || parseTag());

        if (parseAndSkipEL()) {
            return true;
        }
        //rollback
        setPosition(startPos);
        return false;
    }

    // _ -> TagNode
    private boolean parseTag() {
        Position startPos = getPosition();

        if (isEOF() || !parseAndSkipChar(TAG_CHAR)) {
            //rollback
            setPosition(startPos);
            return false;
        }

        var builder = new StringBuilder();

        while (!isEOF() && (Character.isAlphabetic(peek()) || Character.isDigit(peek()))) {
            builder.append(next());
        }

        if (builder.length() == 0) {
            //rollback
            setPosition(startPos);
            return false;
        }

        var tagNode = new TagNode(builder.toString());
        tagNode.setStartPosition(startPos);
        tagNode.setEndPosition(getPosition());
        addNode(tagNode);
        return true;
    }

    // (TagNode+)? TextNode StepNode* -> ScenarioNode
    private boolean parseScenario() {
        Position startPos = getPosition();
        int startNodesCount = getNodes().size();

        var scenarioNode = new ScenarioNode();

        // optional - parse tags
        if (parseTags()) {
            int endNodesCount = getNodes().size();

            for (int i = startNodesCount; i < endNodesCount; i++) {
                var tagNode = (TagNode) getNode(i);
                scenarioNode.addChild(tagNode);
            }

            for (int i = 0; i < endNodesCount - startNodesCount; i++) {
                removeNode(getNodes().size() - 1);
            }
        }

        if (parseAndSkipString("Scenario:") && parseText() && parseMany(this::parseStep)) {
            int endNodesCount = getNodes().size();

            var textNode = getNode(startNodesCount, TextNode.class);
            scenarioNode.addChild(textNode);

            for (int i = startNodesCount + 1; i < endNodesCount; i++) {
                var stepNode = (StepNode) getNode(i);
                scenarioNode.addChild(stepNode);
            }

            for (int i = 0; i < endNodesCount - startNodesCount; i++) {
                removeNode(getNodes().size() - 1);
            }

            addNode(scenarioNode);
            return true;
        }

        // else revert
        setPosition(startPos);

        while (startNodesCount != getNodes().size()) {
            removeNode(getNodes().size() - 1);
        }

        return false;
    }


    // TextNode -> StepNode
    private boolean parseStep() {
        Position startPos = getPosition();

        if (!parseAndSkipChar('-') || !parseText()) {
            //rollback
            setPosition(startPos);
            return false;
        }

        var textNode = getNode(getNodes().size() - 1, TextNode.class);
        removeNode(getNodes().size() - 1);

        var stepNode = new StepNode(textNode.getText());
        addNode(stepNode);
        return true;
    }


    // TextNode -> CommentNode
    private boolean parseComment() {
        Position startPos = getPosition();

        if (!parseAndSkipChar('#') || !parseText()) {
            //rollback
            setPosition(startPos);
            return false;
        }

        var textNode = getNode(getNodes().size() - 1, TextNode.class);
        removeNode(getNodes().size() - 1);

        var commentNode = new CommentNode(textNode.getText());
        addNode(commentNode);
        return true;
    }

    // _ -> TextNode
    private boolean parseText() {
        StringBuilder builder = new StringBuilder();

        while (!isEOF() && !parseAndSkipEL()) {
            builder.append(next());
        }

        if (builder.length() == 0) {
            return false;
        }

        var textNode = new TextNode(builder.toString());
        addNode(textNode);
        return true;
    }

    private boolean parseAndSkipEL() {
        Position startPos = getPosition();

        if (!isEOF() && peek() == '\r') {
            next();
        }

        if (!isEOF() && peek() == '\n') {
            next();
            return true;
        }

        setPosition(startPos);
        return false;
    }

}
