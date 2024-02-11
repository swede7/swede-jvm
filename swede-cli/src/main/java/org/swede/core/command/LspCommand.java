//package org.swede.core.command;
//
//
//import org.swede.lsp.SwedeLanguageServerLauncher;
//
//import java.util.concurrent.ExecutionException;
//
//import static picocli.CommandLine.Command;
//import static picocli.CommandLine.Option;
//
//@Command(name = "lsp")
//public class LspCommand implements Runnable {
//    @Option(names = {"--stdio"})
//    private boolean stdio;
//
//    @Override
//    public void run() {
//        try {
//            SwedeLanguageServerLauncher.startServer(System.in, System.out);
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
