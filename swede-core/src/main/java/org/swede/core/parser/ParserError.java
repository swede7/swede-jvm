package org.swede.core.parser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swede.core.common.Position;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParserError {
    private Position startPosition;
    private Position endPosition;
    private String message;
}
