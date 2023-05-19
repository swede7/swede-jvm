package org.swede.cli.command;


import org.swede.cli.lsp.SwedeLanguageServerLauncher;

import java.util.concurrent.ExecutionException;

import static picocli.CommandLine.Command;

@Command(name = "lsp")
public class LspCommand implements Runnable {
    @Override
    public void run() {
        try {
            SwedeLanguageServerLauncher.startServer(System.in, System.out);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
