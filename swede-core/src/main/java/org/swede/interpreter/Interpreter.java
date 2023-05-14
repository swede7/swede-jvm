package org.swede.interpreter;

import org.swede.api.Step;
import org.swede.ast.DocumentNode;
import org.swede.ast.ScenarioNode;
import org.swede.interpreter.context.FeatureContext;
import org.swede.interpreter.context.ScenarioContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Interpreter {

    private final Map<String, Action> actionMap = new HashMap<>();

    public void registerActionClass(Class<?> actionClass) {

        Object actionClassObject;
        try {
            actionClassObject = actionClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
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
        return (featureContext, scenarioContext) -> {
            try {
                var methodParameters = method.getParameters();

                var args = new ArrayList<>();

                for (var parameter : methodParameters) {
                    if (parameter.getType().equals(FeatureContext.class)) {
                        args.add(featureContext);
                    } else if (parameter.getType().equals(ScenarioContext.class)) {
                        args.add(scenarioContext);
                    } else {
                        throw new RuntimeException("Unsupported parameter type");
                    }
                }

                method.invoke(actionClassObj, args.toArray());
            } catch (InvocationTargetException e) {
                return new ActionResult(ActionResult.ResultStatus.ERROR);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new ActionResult(ActionResult.ResultStatus.OK);
        };
    }

    public void execute(DocumentNode node) {
        var featureContext = new FeatureContext();

        for (var scenarioNode : node.getScenariosNodes()) {
            executeScenario(scenarioNode, featureContext);
        }
    }

    private void executeScenario(ScenarioNode scenario, FeatureContext featureContext) {
        System.out.println("executing scenario: " + scenario.getDescription());

        var scenarioContext = new ScenarioContext();
        for (var stepNode : scenario.getSteps()) {
            executeStep(stepNode, scenarioContext, featureContext);
        }
    }

    private void executeStep(String stepName, ScenarioContext scenarioContext, FeatureContext featureContext) {
        System.out.println("executing step: " + stepName);

        var action = actionMap.get(stepName);
        var result = action.execute(featureContext, scenarioContext);
        System.out.printf("status: %s, message: %s%n", result.getStatus(), result.getMessage());
    }
}
