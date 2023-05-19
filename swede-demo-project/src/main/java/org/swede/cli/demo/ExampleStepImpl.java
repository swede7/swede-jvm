package org.swede.cli.demo;

import org.swede.cli.api.Step;
import org.swede.cli.interpreter.context.ScenarioContext;

public class ExampleStepImpl {
    @Step("Print scenario var")
    public void printHello(ScenarioContext scenarioContext) {
        var scenarioVar = scenarioContext.get("var", Integer.class);
        System.out.println(scenarioVar);
    }

    @Step("Set scenario var = 5")
    public void setScenarioVar5(ScenarioContext scenarioContext) {
        scenarioContext.put("var", 5);
    }

    @Step("Set scenario var = 3")
    public void setScenarioVar3(ScenarioContext scenarioContext) {
        scenarioContext.put("var", 3);
    }
}
