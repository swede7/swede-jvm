package org.swede.core.interpreter;

import lombok.Data;

@Data
public class StepExecutionResult {

    private ResultStatus status;
    private String message;


    public StepExecutionResult(ResultStatus status) {
        this.status = status;
    }

    public StepExecutionResult(ResultStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public enum ResultStatus {
        OK, ERROR, TIMEOUT
    }
}
