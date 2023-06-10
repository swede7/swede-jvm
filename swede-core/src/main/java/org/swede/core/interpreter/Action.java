package org.swede.core.interpreter;

import org.swede.core.interpreter.context.FeatureContext;
import org.swede.core.interpreter.context.ScenarioContext;

@FunctionalInterface
public interface Action {
    ActionResult execute(FeatureContext context, ScenarioContext scenarioContext);
}
