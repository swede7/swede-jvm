package org.swede.interpreter;

@FunctionalInterface
public interface Action {
    ActionResult execute(ActionContext context);
}
