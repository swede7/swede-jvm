package org.swede.core.highlighter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.swede.core.common.Position;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    Position position;
    TokenType type;
}
