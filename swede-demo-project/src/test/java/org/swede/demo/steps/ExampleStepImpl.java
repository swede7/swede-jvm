package org.swede.demo.steps;

import org.swede.core.api.Step;
import org.swede.core.interpreter.context.ScenarioContext;

public class ExampleStepImpl {
    @Step("print <message>")
    public void printHelloWorld(String message) {
        System.out.println(message);
    }

    @Step("save message <value> to context")
    public void saveValueToContext(String message, ScenarioContext scenarioContext) {
        scenarioContext.put("value", message);
    }

    @Step("print message from context")
    public void printMessageFromContext(ScenarioContext scenarioContext) {
        System.out.println(scenarioContext.get("value"));
    }
}
