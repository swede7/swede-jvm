package org.swede.demo.steps;

import org.swede.core.api.Step;
import org.swede.core.interpreter.context.ScenarioContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorStepImpl {
    @Step("Add <a> and <b>")
    public void addTwoNumbers(int a, int b, ScenarioContext scenarioContext) {
        System.out.println("Adding " + a + " and " + b);
        scenarioContext.put("result", a + b);
    }

    @Step("Check that result is <value>")
    public void checkThatResultIs(int value, ScenarioContext scenarioContext) {
        System.out.println("Checking that result is " + value);
        assertEquals(value, scenarioContext.get("result"));
    }
}
