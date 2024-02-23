package org.swede.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.swede.core.formatter.Formatter;
import org.swede.core.highlighter.Highlighter;
import org.swede.core.highlighter.Token;
import org.swede.core.lexer.Lexer;
import org.swede.core.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * TextDocumentService implementation for Swede.
 */
public class SwedeTextDocumentService implements TextDocumentService {

    private final LSClientLogger clientLogger;
    private final SwedeLanguageServer swedeLanguageServer;

    public SwedeTextDocumentService(SwedeLanguageServer swedeLanguageServer) {
        this.swedeLanguageServer = swedeLanguageServer;
        this.clientLogger = LSClientLogger.getInstance();
    }

    @Override
    public void didOpen(DidOpenTextDocumentParams didOpenTextDocumentParams) {
        var code = didOpenTextDocumentParams.getTextDocument().getText();
        CodeHolder.setCode(code);
        this.clientLogger.logMessage("Operation '" + "text/didOpen" + "' {fileUri: '" + didOpenTextDocumentParams.getTextDocument().getUri() + "'} opened");
    }

    @Override
    public void didChange(DidChangeTextDocumentParams didChangeTextDocumentParams) {
        var textDocumentContentChangeEvent = didChangeTextDocumentParams.getContentChanges().get(0);
        var newCode = textDocumentContentChangeEvent.getText();
        CodeHolder.setCode(newCode);
        this.clientLogger.logMessage("Operation '" + "text/didChange" + "' {fileUri: '" + didChangeTextDocumentParams.getTextDocument().getUri() + "'} Changed");
    }

    @Override
    public void didClose(DidCloseTextDocumentParams didCloseTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didClose" + "' {fileUri: '" + didCloseTextDocumentParams.getTextDocument().getUri() + "'} Closed");
    }

    @Override
    public void didSave(DidSaveTextDocumentParams didSaveTextDocumentParams) {
        this.clientLogger.logMessage("Operation '" + "text/didSave" + "' {fileUri: '" + didSaveTextDocumentParams.getTextDocument().getUri() + "'} Saved");
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
            return Either.forLeft(List.of(completionItem));
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
            String code = CodeHolder.getCode();
            String formattedCode;
            try {
                formattedCode = Formatter.format(code);
            } catch (RuntimeException e) {
                formattedCode = code;
                swedeLanguageServer.getLanguageClient().showMessage(new MessageParams(MessageType.Error, e.getMessage()));
            }

            String[] codeLines = code.split("\\R");

            Position startPosition = new Position(0, 0);
            Position endPosition = new Position(codeLines.length - 1, codeLines[codeLines.length - 1].length());
            Range range = new Range(startPosition, endPosition);

            return List.of(new TextEdit(range, formattedCode));
        });
    }

    @Override
    public CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params) {
        return CompletableFuture.supplyAsync(() -> {
            String code = CodeHolder.getCode();

            Lexer lexer = new Lexer(code);
            Parser parser = new Parser(lexer.scan());
            var parseResult = parser.parse();

            var diagnosticItems = new ArrayList<Diagnostic>();

            for (var error : parseResult.getErrors()) {
                var diagnostic = new Diagnostic();
                diagnostic.setSeverity(DiagnosticSeverity.Error);
                diagnostic.setMessage(error.getMessage());

                var startPos = error.getStartPosition();
                var endPos = error.getEndPosition();
                diagnostic.setRange(new Range(new Position(startPos.line(), startPos.column()), new Position(endPos.line(), endPos.column() + 1)));
                diagnosticItems.add(diagnostic);
            }

            RelatedFullDocumentDiagnosticReport relatedFullDocumentDiagnosticReport = new RelatedFullDocumentDiagnosticReport();
            relatedFullDocumentDiagnosticReport.setItems(diagnosticItems);
            return new DocumentDiagnosticReport(relatedFullDocumentDiagnosticReport);
        });
    }


    @Override
    public CompletableFuture<SemanticTokens> semanticTokensFull(SemanticTokensParams params) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String code = CodeHolder.getCode();
                List<Token> tokens = Highlighter.highlight(code);
                return TokenMapper.mapTokens(tokens);
            } catch (Exception e) {
                return new SemanticTokens(List.of());
            }
        });
    }

    @Override
    public CompletableFuture<DocumentDiagnosticReport> diagnostic(DocumentDiagnosticParams params) {
        return CompletableFuture.supplyAsync(() -> {
            String code = CodeHolder.getCode();
            Lexer lexer = new Lexer(code);
            Parser parser = new Parser(lexer.scan());

            var parserResult = parser.parse();
            var errors = parserResult.getErrors();

            List<Diagnostic> diagnostics = new ArrayList<>();

            for (var error : errors) {
                var diagnostic = new Diagnostic();
                diagnostic.setMessage(error.getMessage());

                var range = new Range();
                range.setStart(new Position(error.getStartPosition().line(), error.getStartPosition().column()));
                range.setEnd(new Position(error.getEndPosition().line(), error.getEndPosition().column() + 1));
                diagnostic.setRange(range);

                diagnostics.add(diagnostic);
            }


            var relatedFullDocumentDiagnosticReport = new RelatedFullDocumentDiagnosticReport();
            relatedFullDocumentDiagnosticReport.setItems(diagnostics);

            return new DocumentDiagnosticReport(relatedFullDocumentDiagnosticReport);
        });
    }
}
