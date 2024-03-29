package org.swede.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Language Server implementation for Swede.
 */
public class SwedeLanguageServer implements LanguageServer, LanguageClientAware {

    private final TextDocumentService textDocumentService;
    private final WorkspaceService workspaceService;
    private ClientCapabilities clientCapabilities;
    private LanguageClient languageClient;
    private int shutdown = 1;

    public LanguageClient getLanguageClient() {
        return languageClient;
    }

    public SwedeLanguageServer() {
        this.workspaceService = new SwedeWorkspaceService(this);
        this.textDocumentService = new SwedeTextDocumentService(this);
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
        final InitializeResult response = new InitializeResult(new ServerCapabilities());

        response.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
        this.clientCapabilities = initializeParams.getCapabilities();

        if (!isDynamicCompletionRegistration()) {
            response.getCapabilities().setCompletionProvider(new CompletionOptions());
        }

        if (!isDynamicSemanticTokensRegistration()) {
            SemanticTokensWithRegistrationOptions semanticTokensWithRegistrationOptions = new SemanticTokensWithRegistrationOptions();
            semanticTokensWithRegistrationOptions.setFull(true);

            var legend = new SemanticTokensLegend();
            legend.setTokenTypes(List.of(SemanticTokenTypes.Modifier, SemanticTokenTypes.Comment, SemanticTokenTypes.String, SemanticTokenTypes.Keyword));
            semanticTokensWithRegistrationOptions.setLegend(legend);

            response.getCapabilities().setSemanticTokensProvider(semanticTokensWithRegistrationOptions);
        }

        response.getCapabilities().setDocumentFormattingProvider(new DocumentFormattingOptions());
        response.getCapabilities().setDiagnosticProvider(new DiagnosticRegistrationOptions());

        return CompletableFuture.supplyAsync(() -> response);
    }

    @Override
    public void initialized(InitializedParams params) {
        //Check if dynamic completion support is allowed, if so register.
        if (isDynamicCompletionRegistration()) {
            CompletionRegistrationOptions completionRegistrationOptions = new CompletionRegistrationOptions();
            Registration completionRegistration = new Registration(UUID.randomUUID().toString(),
                    "textDocument/completion", completionRegistrationOptions);
            languageClient.registerCapability(new RegistrationParams(List.of(completionRegistration)));
        }

        if (isDynamicSemanticTokensRegistration()) {
            SemanticTokensWithRegistrationOptions semanticTokensWithRegistrationOptions = new SemanticTokensWithRegistrationOptions();
            semanticTokensWithRegistrationOptions.setFull(true);

            var legend = new SemanticTokensLegend();
            legend.setTokenTypes(List.of(SemanticTokenTypes.Modifier, SemanticTokenTypes.Comment, SemanticTokenTypes.String, SemanticTokenTypes.Keyword));
            semanticTokensWithRegistrationOptions.setLegend(legend);

            Registration semanticTokensWithRegistration = new Registration(UUID.randomUUID().toString(),
                    "textDocument/semanticTokens", semanticTokensWithRegistrationOptions);
            languageClient.registerCapability(new RegistrationParams(List.of(semanticTokensWithRegistration)));
        }
    }

    @Override
    public CompletableFuture<Object> shutdown() {
        shutdown = 0;
        return CompletableFuture.supplyAsync(Object::new);
    }

    @Override
    public void exit() {
        System.exit(shutdown);
    }

    @Override
    public TextDocumentService getTextDocumentService() {
        return this.textDocumentService;
    }

    @Override
    public WorkspaceService getWorkspaceService() {
        return this.workspaceService;
    }

    @Override
    public void connect(LanguageClient client) {
        this.languageClient = client;
        LSClientLogger.getInstance().initialize(this.languageClient);
    }

    private boolean isDynamicCompletionRegistration() {
        TextDocumentClientCapabilities textDocumentCapabilities =
                clientCapabilities.getTextDocument();
        return textDocumentCapabilities != null && textDocumentCapabilities.getCompletion() != null
                && Boolean.TRUE.equals(textDocumentCapabilities.getCompletion().getDynamicRegistration());
    }

    private boolean isDynamicSemanticTokensRegistration() {
        TextDocumentClientCapabilities textDocumentCapabilities =
                clientCapabilities.getTextDocument();
        return textDocumentCapabilities != null && textDocumentCapabilities.getSemanticTokens() != null
                && Boolean.TRUE.equals(textDocumentCapabilities.getSemanticTokens().getDynamicRegistration());
    }


}
