package org.swede.parser;

import org.swede.ast.*;

public class Parser extends AbstractParser {

    public static final char TAG_CHAR = '@';


    public Parser(String code) {
        super(code);
    }


    @Override
    public void parse() {
        parseFeature();
        System.out.println(nodes);
    }

    private boolean parseFeature() {
        int startPos = pos;

        if (parseTags() && parseAndSkipString("Feature:") && parseText() && parseMany(this::parseExpression)) {
            return true;
        }

        if (parseAndSkipString("Feature:") && parseText() && parseMany(this::parseExpression)) {
            return true;
        }

        // rollback
        pos = startPos;
        return false;

    }

    // _ -> (CommentNode | ScenarioNode)*
    private boolean parseExpression() {
        int startPos = pos;

        if (isEOF() || !parseMany(() -> parseComment() || parseScenario() || parseAndSkipNL())) {
            // else rollback
            pos = startPos;
            return false;
        }

        return true;
    }

    private boolean parseAndSkipNL() {
        int startPos = pos;

        if (!isEOF() && parseMany(() -> parseAndSkipChar(' ') || parseAndSkipChar('\n') || parseAndSkipChar('\r'))) {
            return true;
        }
        // else rollback
        pos = startPos;
        return false;
    }

    // _ -> TagNode+
    private boolean parseTags() {
        int startPos = pos;

        if (!parseTag()) {
            // else rollback
            pos = startPos;
            return false;
        }

        parseMany(() -> parseAndSkipChar(' ') || parseTag());

        if (parseAndSkipChar('\n')) {
            return true;
        }
        //rollback
        pos = startPos;
        return false;
    }

    // _ -> TagNode
    private boolean parseTag() {
        int startPos = pos;

        if (isEOF() || !parseAndSkipChar(TAG_CHAR)) {
            //rollback
            pos = startPos;
            return false;
        }

        var builder = new StringBuilder();

        while (!isEOF() && (Character.isAlphabetic(peek()) || Character.isDigit(peek()))) {
            builder.append(next());
        }

        if (builder.length() == 0) {
            //rollback
            pos = startPos;
            return false;
        }

        var tagNode = new TagNode(builder.toString());
        nodes.add(tagNode);
        return true;
    }

    // (TagNode+)? TextNode StepNode* -> ScenarioNode
    private boolean parseScenario() {
        int startPos = pos;
        int startNodesCount = nodes.size();

        var scenarioNode = new ScenarioNode();

        // optional - parse tags
        if (parseTags()) {
            int endNodesCount = nodes.size();

            for (int i = startNodesCount; i < endNodesCount; i++) {
                var tagNode = (TagNode) nodes.get(i);
                scenarioNode.addChild(tagNode);
            }

            for (int i = 0; i < endNodesCount - startNodesCount; i++) {
                nodes.remove(nodes.size() - 1);
            }
        }

        if (parseAndSkipString("Scenario:") && parseText() && parseMany(this::parseStep)) {
            int endNodesCount = nodes.size();

            var textNode = (TextNode) nodes.get(startNodesCount);
            scenarioNode.addChild(textNode);

            for (int i = startNodesCount + 1; i < endNodesCount - 1; i++) {
                var stepNode = (StepNode) nodes.get(i);
                scenarioNode.addChild(stepNode);
            }

            for (int i = 0; i < endNodesCount - startNodesCount; i++) {
                nodes.remove(nodes.size() - 1);
            }

            nodes.add(scenarioNode);
            return true;
        }

        // else revert
        pos = startPos;

        while (startNodesCount != nodes.size()) {
            nodes.remove(nodes.size() - 1);
        }

        return false;
    }

    private boolean parseAndSkipString(String s) {
        int startPos = pos;

        int i = 0;
        while (!isEOF() && i < s.length() && s.charAt(i) == peek()) {
            pos++;
            i++;
        }

        if (i == s.length()) {
            return true;
        }
        // else rollback
        pos = startPos;
        return false;
    }

    // TextNode -> StepNode
    private boolean parseStep() {
        int startPos = pos;

        if (!parseAndSkipChar('-') || !parseText()) {
            //rollback
            pos = startPos;
            return false;
        }

        var textNode = (TextNode) nodes.get(nodes.size() - 1);
        nodes.remove(nodes.size() - 1);

        var stepNode = new StepNode(textNode.getText());
        nodes.add(stepNode);
        return true;
    }


    // TextNode -> CommentNode
    private boolean parseComment() {
        int startPos = pos;

        if (!parseAndSkipChar('#') || !parseText()) {
            //rollback
            pos = startPos;
            return false;
        }

        var textNode = (TextNode) nodes.get(nodes.size() - 1);
        nodes.remove(nodes.size() - 1);

        var commentNode = new CommentNode(textNode.getText());
        nodes.add(commentNode);
        return true;
    }

    // _ -> TextNode
    private boolean parseText() {
        StringBuilder builder = new StringBuilder();

        char currentChar = next();
        while (!isEOF() && currentChar != '\n') {
            builder.append(currentChar);
            currentChar = next();
        }

        if (builder.length() == 0) {
            return false;
        }

        var textNode = new TextNode(builder.toString());
        nodes.add(textNode);
        return true;
    }

}
