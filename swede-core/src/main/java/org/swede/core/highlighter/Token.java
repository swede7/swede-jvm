package org.swede.core.highlighter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swede.core.common.Position;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    private Position startPosition;
    private Position endPosition;
    private TokenType type;
}
