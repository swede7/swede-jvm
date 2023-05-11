package org.swede.interpreter;

import java.lang.reflect.InvocationTargetException;

@FunctionalInterface
public interface Action {
    ActionResult execute(ActionContext context);
}
