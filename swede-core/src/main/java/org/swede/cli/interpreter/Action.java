package org.swede.cli.interpreter;

import org.swede.cli.interpreter.context.FeatureContext;
import org.swede.cli.interpreter.context.ScenarioContext;

@FunctionalInterface
public interface Action {
    ActionResult execute(FeatureContext context, ScenarioContext scenarioContext);
}
