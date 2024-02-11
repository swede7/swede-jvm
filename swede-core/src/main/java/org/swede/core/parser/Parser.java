package org.swede.core.parser;


import org.swede.core.ast.Node;
import org.swede.core.ast.Node.NodeType;
import org.swede.core.lexer.Lexeme;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class Parser extends AbstractParser {

    private final List<Supplier<Boolean>> RULES = List.of(
            this::addTagToFeatureRule,
            this::addTagToScenarioRule,
            this::addStepToScenarioRule,
            this::skipSpacesAndNlRule,
            this::commentRule,
            this::tagRule,
            this::featureRule,
            this::scenarioRule,
            this::stepRule,
            this::handleUnexpectedLexemeRule,
            this::handleUnexpectedNodesRule
    );


    public Parser(List<Lexeme> tokens) {
        super(tokens);
    }


    public Node parse() {
        while (true) {
            var anyRuleWasApplied = false;
            for (var rule : RULES) {
                if (rule.get()) {
                    anyRuleWasApplied = true;
                    break;
                }
            }
            if (!anyRuleWasApplied) {
                break;
            }
        }

        return null;
    }


    private boolean skipSpacesAndNlRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() == Lexeme.LexemeType.SPACE || peekLexeme().getType() == Lexeme.LexemeType.NL) {
            advance();
            return true;
        }
        return false;
    }

    private boolean tagRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() != Lexeme.LexemeType.AT_CHR) {
            return false;
        }

        if (lexemesLeft() <= 1) {
            return false;
        }

        if (lookup(1).getType() != Lexeme.LexemeType.WORD) {
            return false;
        }

        var atLexeme = peekLexeme();
        var wordLexeme = lookup(1);

        var tagNode = new Node(NodeType.TAG, atLexeme.getStartPosition(), wordLexeme.getEndPosition(), wordLexeme.getValue());
        addNode(tagNode);

        advance(2);
        return true;
    }

    private boolean commentRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() != Lexeme.LexemeType.HASH_CHR) {
            return false;
        }
        var hashLexeme = peekLexeme();

        StringBuilder sb = new StringBuilder();
        advance();

        while (!isEof() && peekLexeme().getType() != Lexeme.LexemeType.NL) {
            var currentLexeme = peekLexeme();
            sb.append(currentLexeme.getValue());
            advance();
        }

        var commentNode = new Node(NodeType.COMMENT, hashLexeme.getStartPosition(), getPreviousLexeme().getEndPosition(), sb.toString());
        addNode(commentNode);
        return true;
    }

    private boolean featureRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() != Lexeme.LexemeType.FEATURE_WORD) {
            return false;
        }
        var featureWordLexeme = peekLexeme();

        StringBuilder sb = new StringBuilder();
        advance();

        while (!isEof() && peekLexeme().getType() != Lexeme.LexemeType.NL) {
            var currentLexeme = peekLexeme();
            sb.append(currentLexeme.getValue());
            advance();
        }

        var featureNode = new Node(NodeType.FEATURE, featureWordLexeme.getStartPosition(), getPreviousLexeme().getEndPosition(), sb.toString());
        addNode(featureNode);
        return true;
    }

    private boolean addTagToFeatureRule() {
        var nodes = getNodes();
        if (nodes.size() < 2) {
            return false;
        }

        var previousNode = nodes.get(nodes.size() - 2);
        var currentNode = nodes.get(nodes.size() - 1);

        if (previousNode.getType() == NodeType.TAG && currentNode.getType() == NodeType.FEATURE) {// add tag to feature node
            currentNode.prependChild(previousNode);
            getNodes().remove(nodes.size() - 2);
            return true;
        }

        return false;
    }

    private boolean scenarioRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() != Lexeme.LexemeType.SCENARIO_WORD) {
            return false;
        }
        var scenarioWordLexeme = peekLexeme();

        StringBuilder sb = new StringBuilder();
        advance();

        while (!isEof() && peekLexeme().getType() != Lexeme.LexemeType.NL) {
            var currentLexeme = peekLexeme();
            sb.append(currentLexeme.getValue());
            advance();
        }

        var featureNode = new Node(NodeType.SCENARIO, scenarioWordLexeme.getStartPosition(), getPreviousLexeme().getEndPosition(), sb.toString());
        addNode(featureNode);
        return true;
    }

    private boolean addTagToScenarioRule() {
        var nodes = getNodes();
        if (nodes.size() < 2) {
            return false;
        }

        var previousNode = nodes.get(nodes.size() - 2);
        var currentNode = nodes.get(nodes.size() - 1);

        if (previousNode.getType() == NodeType.TAG && currentNode.getType() == NodeType.SCENARIO) {// add tag to feature node
            currentNode.prependChild(previousNode);
            getNodes().remove(nodes.size() - 2);
            return true;
        }

        return false;
    }

    private boolean stepRule() {
        if (isEof()) {
            return false;
        }

        if (peekLexeme().getType() != Lexeme.LexemeType.DASH_CHR) {
            return false;
        }
        var dashLexeme = peekLexeme();

        StringBuilder sb = new StringBuilder();
        advance();

        while (!isEof() && peekLexeme().getType() != Lexeme.LexemeType.NL) {
            var currentLexeme = peekLexeme();
            sb.append(currentLexeme.getValue());
            advance();
        }

        var stepNode = new Node(NodeType.STEP, dashLexeme.getStartPosition(), getPreviousLexeme().getEndPosition(), sb.toString());
        addNode(stepNode);
        return true;
    }

    private boolean addStepToScenarioRule() {
        var nodes = getNodes();
        if (nodes.size() < 2) {
            return false;
        }

        var previousNode = nodes.get(nodes.size() - 2);
        var currentNode = nodes.get(nodes.size() - 1);

        if (previousNode.getType() == NodeType.SCENARIO && currentNode.getType() == NodeType.STEP) {// add tag to feature node
            previousNode.appendChild(currentNode);
            getNodes().remove(nodes.size() - 1);
            return true;
        }
        return false;
    }

    private boolean handleUnexpectedLexemeRule() {
        if (isEof()) {
            return false;
        }

        var unexpectedLexeme = peekLexeme();
        var unexpectedNode = new Node(NodeType.UNEXPECTED, unexpectedLexeme.getStartPosition(), unexpectedLexeme.getEndPosition(), unexpectedLexeme.getValue());

        addNode(unexpectedNode);
        addError(unexpectedLexeme.getStartPosition(), unexpectedLexeme.getEndPosition(), "unexpected lexeme");
        advance();
        return true;
    }

    private boolean handleUnexpectedNodesRule() {
        if (!isEof()) {
            return false;
        }

        var nodes = getNodes();
        final Set<NodeType> validNodeTypes = Set.of(NodeType.UNEXPECTED, NodeType.COMMENT, NodeType.FEATURE, NodeType.SCENARIO);

        boolean someNodesWasProcessed = false;
        for (var node : nodes) {
            if (!validNodeTypes.contains(node.getType())) {
                node.setType(NodeType.UNEXPECTED);
                addError(node.getStartPosition(), node.getEndPosition(), "unexpected node");
                someNodesWasProcessed = true;
            }
        }

        return someNodesWasProcessed;
    }


}
