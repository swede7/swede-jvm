package org.swede.core.lsp;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Language Server implementation for Swede.
 */
public class SwedeLanguageServer implements LanguageServer, LanguageClientAware {

    private TextDocumentService textDocumentService;
    private WorkspaceService workspaceService;
    private ClientCapabilities clientCapabilities;
    LanguageClient languageClient;
    private int shutdown = 1;

    public SwedeLanguageServer() {
        this.textDocumentService = new SwedeTextDocumentService(this);
        this.workspaceService = new SwedeWorkspaceService(this);
    }

    @Override
    public CompletableFuture<InitializeResult> initialize(InitializeParams initializeParams) {
        final InitializeResult response = new InitializeResult(new ServerCapabilities());
        //Set the document synchronization capabilities to full. 
        response.getCapabilities().setTextDocumentSync(TextDocumentSyncKind.Full);
        this.clientCapabilities = initializeParams.getCapabilities();
        
        /* Check if dynamic registration of completion capability is allowed by the client. If so we don't register the capability.
           Else, we register the completion capability.  
         */
        if (!isDynamicCompletionRegistration()) {
            response.getCapabilities().setCompletionProvider(new CompletionOptions());
        }

        if (!isDynamicSemanticTokensRegistration()) {
            response.getCapabilities().setSemanticTokensProvider(new SemanticTokensWithRegistrationOptions());
        }

        response.getCapabilities().setDocumentFormattingProvider(new DocumentFormattingOptions());

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
            legend.setTokenTypes(List.of(SemanticTokenTypes.Modifier, SemanticTokenTypes.Comment));
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
    public void connect(LanguageClient languageClient) {
        this.languageClient = languageClient;
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
