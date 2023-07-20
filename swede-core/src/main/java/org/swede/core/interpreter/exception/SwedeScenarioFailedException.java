package org.swede.core.interpreter.exception;

public class SwedeScenarioFailedException extends Exception {
    public SwedeScenarioFailedException() {
        super("some of the scenarios failed.");
    }
}
