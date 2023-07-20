package org.swede.core.interpreter;

import lombok.Data;

@Data
public class StepDefinition {
    private String text;
    private String regex;
    private int parametersCount;
}
