package org.swede.core.interpreter;

import org.swede.core.interpreter.context.FeatureContext;
import org.swede.core.interpreter.context.ScenarioContext;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class StepImplementationMethodParametersResolver {

    private StepImplementationMethodParametersResolver() {

    }

    /**
     * @param stepParameters           step parameters
     * @param featureContext           feature context that can used in step implementation
     * @param scenarioContext          scenario context that can used in step implementation
     * @param stepImplementationMethod step implementation method
     * @return list of argument values with which the method will be executed.
     */
    public static List<Object> resolve(List<String> stepParameters, FeatureContext featureContext, ScenarioContext scenarioContext, Method stepImplementationMethod) {
        List<Object> result = new ArrayList<>();

        var methodParameters = stepImplementationMethod.getParameters();

        int stepParameterIndex = 0;

        for (var methodParameter : methodParameters) {
            if (methodParameter.getType().equals(FeatureContext.class)) {
                result.add(featureContext);
            } else if (methodParameter.getType().equals(ScenarioContext.class)) {
                result.add(scenarioContext);
            } else if (StepParameterMapper.supportedType(methodParameter.getType())) {
                String stepParameter = stepParameters.get(stepParameterIndex);
                result.add(StepParameterMapper.map(stepParameter, methodParameter.getType()));
                stepParameterIndex++;
            } else {
                throw new RuntimeException("Unsupported parameter type");
            }
        }

        if (stepParameterIndex != stepParameters.size()) {
            throw new RuntimeException("All step parameters should be declared");
        }

        return result;
    }

}
