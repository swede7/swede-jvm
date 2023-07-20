package org.swede.core.interpreter;

import java.util.regex.Pattern;

public final class StepDefinitionParser {

    private StepDefinitionParser() {

    }

    public static StepDefinition parse(String stepDefinitionText) {
        StringBuilder resultStringBuilder = new StringBuilder();
        StringBuilder segmentStringBuilder = new StringBuilder();

        int parametersCount = 0;

        boolean inParameterExpression = false;
        for (int i = 0; i < stepDefinitionText.length(); i++) {
            char c = stepDefinitionText.charAt(i);


            if (c == '<') {
                if (inParameterExpression) {
                    throw new RuntimeException("invalid char");
                }
                //add left segment to result
                addEscapedSegment(segmentStringBuilder.toString(), resultStringBuilder);
                segmentStringBuilder = new StringBuilder();
                inParameterExpression = true;
                continue;
            }

            if (c == '>') {
                if (!inParameterExpression) {
                    throw new RuntimeException("invalid char");
                }

                addParameterPattern(resultStringBuilder);
                inParameterExpression = false;
                parametersCount++;
                continue;
            }

            if (inParameterExpression) {
                //ignore
                continue;
            }
            segmentStringBuilder.append(c);
        }

        if (!segmentStringBuilder.isEmpty()) {
            addEscapedSegment(segmentStringBuilder.toString(), resultStringBuilder);
        }

        String stepRegex = resultStringBuilder.toString();

        var stepDefinition = new StepDefinition();
        stepDefinition.setRegex(stepRegex);
        stepDefinition.setText(stepDefinitionText);
        stepDefinition.setParametersCount(parametersCount);

        return stepDefinition;

    }

    private static void addEscapedSegment(String segment, StringBuilder builder) {
        builder.append(Pattern.quote(segment));
    }

    private static void addParameterPattern(StringBuilder builder) {
        builder.append("\"(.*)\"");
    }
}
