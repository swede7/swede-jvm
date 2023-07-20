package org.swede.core.interpreter;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StepDefinitionParserTest {
    @Test
    public void testParse() {
        var stepDefinition = "Add <first number> and <second number>";
        var result = StepDefinitionParser.parse(stepDefinition);
        assertEquals(2, result.getParametersCount());
        assertEquals("Add <first number> and <second number>", result.getText());
        assertEquals("\\QAdd \\E\"(.*)\"\\Q and \\E\"(.*)\"", result.getRegex());
    }
}
