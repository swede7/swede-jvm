package org.swede.core.lexer;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.swede.core.common.Position;

import javax.annotation.Nullable;

@Data
@AllArgsConstructor
public class Token {
    private TokenType type;
    private Position startPosition;
    private Position endPosition;
    @Nullable
    private String value;


    public enum TokenType {
        NL,
        AT_CHR,
        DASH_CHR,
        HASH_CHR,
        WORD,
        SPACE,
        FEATURE_WORD,
        SCENARIO_WORD,
    }
}
