package org.swede.lsp;

public final class CodeHolder {

    private CodeHolder() {
    }

    private static String code;

    public static String getCode() {
        return code;
    }

    public static void setCode(String code) {
        CodeHolder.code = code;
    }
}
