package org.swede.interpreter;

import org.swede.interpreter.context.FeatureContext;
import org.swede.interpreter.context.ScenarioContext;

@FunctionalInterface
public interface Action {
    ActionResult execute(FeatureContext context, ScenarioContext scenarioContext);
}
