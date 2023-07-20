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

    private final Map<StepDefinition, StepAction> stepActionsMap = new HashMap<>();

    public void registerStepsImplementationClass(Class<?> stepsImplementationClass) {
        Object stepsImplementationClassObject;

        try {
            stepsImplementationClassObject = stepsImplementationClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (var method : stepsImplementationClass.getMethods()) {
            if (method.getAnnotation(Step.class) == null) {
                continue;
            }

            var stepMethodAnnotation = method.getAnnotation(Step.class);
            String stepExpression = stepMethodAnnotation.value();

            StepDefinition stepDefinition = StepDefinitionParser.parse(stepExpression);
            var action = createStepAction(method, stepsImplementationClassObject);

            stepActionsMap.put(stepDefinition, action);
        }
    }

    private StepAction createStepAction(Method stepImplementationMethod, Object stepsImpementationClassObject) {
        return (parameters, featureContext, scenarioContext) -> {
            try {
                List<Object> args = StepImplementationMethodParametersResolver.resolve(parameters, featureContext, scenarioContext, stepImplementationMethod);
                stepImplementationMethod.invoke(stepsImpementationClassObject, args.toArray());
            } catch (InvocationTargetException e) {
                return new StepExecutionResult(StepExecutionResult.ResultStatus.ERROR);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new StepExecutionResult(StepExecutionResult.ResultStatus.OK);
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
        var action = stepActionsMap.get(stepDefinition);

        List<String> stepParameters = getStepParameters(stepDefinition, stepExpression);

        var result = action.execute(stepParameters, featureContext, scenarioContext);
        return result.getStatus().equals(StepExecutionResult.ResultStatus.OK);
    }

    private StepDefinition findStepDefinition(String step) {
        for (var entry : stepActionsMap.entrySet()) {
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

        if (matcher.find()) {
            throw new IllegalStateException();
        }

        for (int i = 1; i <= matcher.groupCount(); i++) {
            result.add(matcher.group(i));
        }
        return result;
    }
}
