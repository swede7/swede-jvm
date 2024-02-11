package org.swede.core.parser;

import org.junit.jupiter.api.Test;
import org.swede.core.lexer.Lexer;

class ParserTest {

    @Test
    public void test() {
        String code = """
                @all
                Feature: Basic calculator operations
                                
                # Comment example
                                
                @pass @automated
                Scenario: Addition
                - Enter "2 + 2"
                - Click on calculation button
                                 bla bla bla
                - Check that the answer is "5"
                                
                @fail
                Scenario: Division by zero
                - Enter "5 / 0"
                - Click on calculation button
                - Аn exception must be thrown
                """;


        Lexer lexer = new Lexer(code);
        Parser parser = new Parser(lexer.scan());

        System.out.println(parser.parse());

    }

}
