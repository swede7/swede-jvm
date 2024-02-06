package org.swede.core.lexer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.swede.core.common.Position;

@Data
@AllArgsConstructor
public class Token {
    private TokenType type;
    private Position startPosition;
    private Position endPosition;
    private String value;


    public static enum TokenType {
        NL,
        AT_CHR,
        DASH_CHR,
        WORD,
        SPACE,
        FEATURE_WORD,
        SCENARIO_WORD,
    }
}
