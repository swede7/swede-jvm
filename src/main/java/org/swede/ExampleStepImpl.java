package org.swede;

import org.swede.api.Step;

public class ExampleStepImpl {
    @Step(" Print hello")
    public void printHelloWorld() {
        System.out.println("hello world");
    }
}
