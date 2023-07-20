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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Interpreter {

    private final Map<StepDefinition, Action> actionMap = new HashMap<>();

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

            var stepDefinition = StepDefinitionParser.parse(stepExpression);
            actionMap.put(stepDefinition, action);
        }
    }

    private Action createAction(Method method, Object actionClassObj) {
        return (parameters, featureContext, scenarioContext) -> {
            try {
                List<Object> methodArgs = ParametersResolver.resolve(parameters, featureContext, scenarioContext, method);

                method.invoke(actionClassObj, methodArgs.toArray());
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

    private boolean executeStep(String stepExpression, ScenarioContext scenarioContext, FeatureContext featureContext) {
        StepDefinition stepDefinition = findStepDefinition(stepExpression);

        if (stepDefinition == null) {
            System.out.println(">>> step with name: " + stepExpression + " not implemented. skipping...");
            return true;
        }
        var action = actionMap.get(stepDefinition);

        List<String> stepParameters = getStepParameters(stepDefinition, stepExpression);

        var result = action.execute(stepParameters, featureContext, scenarioContext);
        return result.getStatus().equals(ActionResult.ResultStatus.OK);
    }

    private StepDefinition findStepDefinition(String step) {
        for (var entry : actionMap.entrySet()) {
            var stepDefinition = entry.getKey();
            var stepDefinitionRegex = stepDefinition.getRegex();
            if (Pattern.matches(stepDefinitionRegex, step)) {
                return stepDefinition;
            }
        }
        return null;
    }

    private List<String> getStepParameters(StepDefinition stepDefinition, String stepExpression) {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile(stepDefinition.getRegex());
        Matcher matcher = pattern.matcher(stepExpression);

        matcher.find();
        for (int i = 1; i <= matcher.groupCount(); i++) {
            result.add(matcher.group(i));
        }
        return result;
    }
}
