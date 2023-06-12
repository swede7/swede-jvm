package org.swede.core.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.swede.core.formatter.Formatter;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TextDocumentService implementation for Swede.
 */
public class SwedeTextDocumentService implements TextDocumentService {

    private SwedeLanguageServer languageServer;
    private LSClientLogger clientLogger;

    public SwedeTextDocumentService(SwedeLanguageServer languageServer) {
        this.languageServer = languageServer;
        this.clientLogger = LSClientLogger.getInstance();
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didOpen" +
                "' {fileUri: '" + didOpenTextDocumentParams.getTextDocument().getUri() + "'} opened");
    }

    @Override
    public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didChange" +
                "' {fileUri: '" + didChangeTextDocumentParams.getTextDocument().getUri() + "'} Changed");
    }

    @Override
    public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didClose" +
                "' {fileUri: '" + didCloseTextDocumentParams.getTextDocument().getUri() + "'} Closed");
    }

    @Override
    public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didSave" +
                "' {fileUri: '" + didSaveTextDocumentParams.getTextDocument().getUri() + "'} Saved");
    }

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
        return CompletableFuture.supplyAsync(() -> {
            this.clientLogger.logMessage("Operation '" + "text/completion");
            CompletionItem completionItem = new CompletionItem();
            completionItem.setLabel("Test completion item");
            completionItem.setInsertText("Test");
            completionItem.setDetail("Snippet");
            completionItem.setKind(CompletionItemKind.Snippet);
            return Either.forLeft(Arrays.asList(completionItem));
        });
    }

    @Override
    public CompletableFuture<CompletionItem> resolveCompletionItem(CompletionItem unresolved) {
        return CompletableFuture.supplyAsync(() -> {
            CompletionItem completionItem = new CompletionItem();
            completionItem.setInsertText(unresolved.getInsertText());
            return completionItem;
        });
    }

    @Override
    public CompletableFuture<List<? extends TextEdit>> formatting(DocumentFormattingParams params) {
        return CompletableFuture.supplyAsync(() -> {
            String code;

            try {
                URI uri = new URI(params.getTextDocument().getUri());
                code = Files.readString(Paths.get(uri), StandardCharsets.UTF_8);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }

            Formatter formatter = new Formatter(code);
            String formattedCode = formatter.format();

            String[] codeLines = code.split("\\R");

            Position startPosition = new Position(0, 0);
            Position endPosition = new Position(codeLines.length - 1, codeLines[codeLines.length - 1].length());
            Range range = new Range(startPosition, endPosition);

            return List.of(new TextEdit(range, formattedCode));
        });
    }
}
