package org.swede.core.ast;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KeywordNode extends AbstractNode {
    private String value;
}
