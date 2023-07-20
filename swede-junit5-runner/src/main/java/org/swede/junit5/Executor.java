package org.swede.junit5;

import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.swede.core.ast.DocumentNode;
import org.swede.core.interpreter.Interpreter;
import org.swede.core.interpreter.exception.SwedeScenarioFailedException;
import org.swede.core.parser.Parser;
import org.swede.junit5.descriptor.SwedeFeatureFileDescriptor;

import java.io.File;

public class Executor {

    private final ExecutionRequest request;
    private final TestDescriptor rootDescriptor;

    public Executor(ExecutionRequest request) {
        this.request = request;
        this.rootDescriptor = request.getRootTestDescriptor();
    }

    public void execute() {
        request.getEngineExecutionListener().executionStarted(rootDescriptor);
        executeSwedeFeatureFiles();
        request.getEngineExecutionListener().executionFinished(rootDescriptor, TestExecutionResult.successful());
    }

    private void executeSwedeFeatureFiles() {
        for (var descriptor : rootDescriptor.getChildren()) {
            request.getEngineExecutionListener().executionStarted(descriptor);

            var swedeFileDescriptor = (SwedeFeatureFileDescriptor) descriptor;

            File featureFile = swedeFileDescriptor.getFeatureFile();
            String code = Utils.readTextFromFile(featureFile);

            if (swedeFileDescriptor.getStepImplClass() == null) {
                request.getEngineExecutionListener().executionSkipped(descriptor, "impl file not found");
                return;
            }

            Parser parser = new Parser(code);
            DocumentNode documentNode = parser.parse();

            Interpreter interpreter = new Interpreter();
            interpreter.registerStepsImplementationClass(swedeFileDescriptor.getStepImplClass());
            boolean status = interpreter.execute(documentNode);

            request.getEngineExecutionListener().executionFinished(descriptor, status ? TestExecutionResult.successful() : TestExecutionResult.failed(new SwedeScenarioFailedException()));
        }
    }
}
