package org.swede.core.parser;

import lombok.Data;
import org.swede.core.ast.Node;

import java.util.List;

@Data
public class ParserResult {
    private Node rootNode;
    private List<ParserError> errors;
}
