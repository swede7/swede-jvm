package org.swede.demo.steps;

import org.swede.core.api.Step;

public class ExampleStepImpl {
    @Step("print hello world")
    public void printHelloWorld() {
        System.out.println("Hello world, swede! \uD83C\uDF31");
    }
}
