package org.swede.lsp;

import lombok.Getter;

public final class CodeHolder {

    private CodeHolder() {
    }

    @Getter
    private static String code;

    public static void setCode(String code) {
        CodeHolder.code = code;
    }
}
