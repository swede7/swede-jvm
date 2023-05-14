package org.swede;

import org.swede.lsp.SwedeLanguageServerLauncher;

import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SwedeLanguageServerLauncher.startServer(System.in, System.out);
    }
}
