package org.swede.cli.interpreter;

import lombok.Data;

@Data
public class ActionResult {

    private ResultStatus status;
    private String message;


    public ActionResult(ResultStatus status) {
        this.status = status;
    }

    public ActionResult(ResultStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public enum ResultStatus {
        OK, ERROR, TIMEOUT
    }
}
