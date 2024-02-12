package org.swede.core.parser;

import org.junit.jupiter.api.Test;
import org.swede.core.lexer.Lexer;

class ParserTest {

    @Test
    public void test() {
        String code = """
                @all
                Feature: Basic calculator operations
                
                @pass @automated
                Scenario: Addition
                - Enter "2 + 2"
                - Click on calculation button
                - Check that the answer is "5"
                
                
                
                Sceanrio: asdasd
                - asdasdasd
                - asdasdasdasdas

                """;


        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer.scan());
        var result = parser.parse();
        System.out.println(result);

    }

}
