package org.swede.core.interpreter;

public class SwedeScenarioFailedException extends Exception {
    public SwedeScenarioFailedException() {
        super("some of the scenarios failed.");
    }
}
