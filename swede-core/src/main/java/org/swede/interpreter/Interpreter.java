package org.swede.interpreter;

import org.swede.api.Step;
import org.swede.ast.DocumentNode;
import org.swede.ast.ScenarioNode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private final Map<String, Action> actionMap = new HashMap<>();

    public void registerActionClass(Class<?> actionClass) {

        Object actionClassObject;
        try {
            actionClassObject = actionClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }

        for (var method : actionClass.getMethods()) {
            if (method.getAnnotation(Step.class) == null) {
                continue;
            }

            var stepAnnotation = method.getAnnotation(Step.class);
            var stepExpression = stepAnnotation.value();

            var action = createAction(method, actionClassObject);
            actionMap.put(stepExpression, action);
        }
    }

    private Action createAction(Method method, Object actionClassObj) {
        return context -> {
            try {
                method.invoke(actionClassObj);
            } catch (InvocationTargetException e) {
                return new ActionResult(ActionResult.ResultStatus.ERROR);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new ActionResult(ActionResult.ResultStatus.OK);
        };
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
