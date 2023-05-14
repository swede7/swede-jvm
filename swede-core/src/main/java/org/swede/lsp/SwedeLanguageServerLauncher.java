package org.swede.lsp;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;
import org.swede.lsp.SwedeLanguageServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Standard IO Launcher for Swede Language Server.
 */
public class SwedeLanguageServerLauncher {

    /**
     * Starts the language server given the input and output streams to read and write messages.
     *
     * @param in  input stream.
     * @param out output stream.
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void startServer(InputStream in, OutputStream out) throws InterruptedException, ExecutionException {
        SwedeLanguageServer server = new SwedeLanguageServer();
        Launcher<LanguageClient> launcher = Launcher.createLauncher(server, LanguageClient.class, in, out);
        LanguageClient client = launcher.getRemoteProxy();
        server.connect(client);
        Future<?> startListening = launcher.startListening();
        startListening.get();
    }
}
