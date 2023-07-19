package org.swede.core.interpreter;

import org.swede.core.api.Step;
import org.swede.core.ast.DocumentNode;
import org.swede.core.ast.ScenarioNode;
import org.swede.core.interpreter.context.FeatureContext;
import org.swede.core.interpreter.context.ScenarioContext;

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

    public boolean execute(DocumentNode node) {
        var featureContext = new FeatureContext();

        boolean status = true;
        for (var scenarioNode : node.getScenariosNodes()) {
            status = status & executeScenario(scenarioNode, featureContext);
        }
        return status;
    }

    private boolean executeScenario(ScenarioNode scenario, FeatureContext featureContext) {
        System.out.println(">>> executing scenario: " + scenario.getDescription());

        var scenarioContext = new ScenarioContext();

        boolean status = true;
        for (var stepNode : scenario.getSteps()) {
            status = status && executeStep(stepNode, scenarioContext, featureContext);
        }

        return status;
    }

    private boolean executeStep(String stepName, ScenarioContext scenarioContext, FeatureContext featureContext) {
        var action = actionMap.get(stepName);

        if (action == null) {
            System.out.println(">>> step with name: " + stepName + " not implemented. skipping...");
            return true;
        }

        var result = action.execute(featureContext, scenarioContext);
        return result.getStatus().equals(ActionResult.ResultStatus.OK);
    }
}
