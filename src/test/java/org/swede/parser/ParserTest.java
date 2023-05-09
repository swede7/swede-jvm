package org.swede.parser;

import org.junit.jupiter.api.Test;
import org.swede.Main;
import org.swede.ast.CommentNode;
import org.swede.ast.ScenarioNode;
import org.swede.ast.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ParserTest {
    @Test
    public void test() throws IOException {
        String code = readFromFile("code/good-case.feature");

        Parser parser = new Parser(code);
        var documentNode = parser.parse();

        assertEquals(" Basic calculator operations", documentNode.getDescription());
        assertEquals(List.of("all", "skip"), documentNode.getTags());

        var commentNode = documentNode.getChildByClass(CommentNode.class).get();
        assertEquals(" This feature defines a set of operations that the calculator must support.", commentNode.getComment());

        var scenarioNodes = documentNode.getChildrenByClass(ScenarioNode.class);

        var firstScenarioNode = scenarioNodes.get(0);
        assertEquals(List.of("pass", "automated"), firstScenarioNode.getTags());

        assertEquals(" Addition", firstScenarioNode.getDescription());
        assertEquals(" Enter \"2 + 2\"", firstScenarioNode.getSteps().get(0));
        assertEquals(" Click on calculation button", firstScenarioNode.getSteps().get(1));
        assertEquals(" Check that the answer is \"5\"", firstScenarioNode.getSteps().get(2));

        var secondScenarioNode = scenarioNodes.get(1);
        assertEquals(List.of("fail"), secondScenarioNode.getTags());

        assertEquals(" Division by zero", secondScenarioNode.getDescription());
        assertEquals(" Enter \"5 / 0\"", secondScenarioNode.getSteps().get(0));
        assertEquals(" Click on calculation button", secondScenarioNode.getSteps().get(1));
        assertEquals(" Аn exception must be thrown", secondScenarioNode.getSteps().get(2));
    }

    private static String readFromFile(String path) throws IOException {
        InputStream is = Main.class.getClassLoader().getResourceAsStream(path);
        assert is != null;
        String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();
        return text;
    }
}
