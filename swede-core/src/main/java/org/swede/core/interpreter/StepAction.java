package org.swede.core.interpreter;

import org.swede.core.interpreter.context.FeatureContext;
import org.swede.core.interpreter.context.ScenarioContext;

import java.util.List;

@FunctionalInterface
public interface StepAction {
    StepExecutionResult execute(List<String> parameters, FeatureContext context, ScenarioContext scenarioContext);
}
