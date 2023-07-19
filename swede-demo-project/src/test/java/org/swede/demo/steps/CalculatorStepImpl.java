package org.swede.demo.steps;

import org.swede.core.api.Step;
import org.swede.core.interpreter.context.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorStepImpl {
    @Step("Add 2 + 2")
    public void addTwoPlusTwo(ScenarioContext scenarioContext) {
        scenarioContext.put("result", 2 + 2);
    }

    @Step("Add 2 + 3")
    public void addTwoPlusThree(ScenarioContext scenarioContext) {
        scenarioContext.put("result", 2 + 3);
    }

    @Step("Check that returned 4")
    public void checkThatReturnedFour(ScenarioContext scenarioContext) {
        assertEquals(4, scenarioContext.get("result"));
    }
}
