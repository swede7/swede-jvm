package org.swede.interpreter;

import org.swede.ast.DocumentNode;
import org.swede.ast.ScenarioNode;

import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private final Map<String, Action> actionMap = new HashMap<>();

    public void registerAction(String actionName, Action action) {
        actionMap.put(actionName, action);
    }

    public void execute(DocumentNode node) {
        for (var scenarioNode : node.getScenariosNodes()) {
            executeScenario(scenarioNode);
        }
    }

    private void executeScenario(ScenarioNode scenario) {
        System.out.println("executing scenario: " + scenario.getDescription());
        var context = new ActionContext();

        for (var stepNode : scenario.getSteps()) {
            executeStep(stepNode, context);
        }
    }

    private void executeStep(String stepName, ActionContext context) {
        System.out.println("executing step: " + stepName);
        var action = actionMap.get(stepName);
        var result = action.execute(context);
        System.out.printf("status: %s, message: %s%n", result.getStatus(), result.getMessage());
    }
}
