package org.swede.junit5.annotation;

import org.junit.platform.commons.annotation.Testable;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Testable
public @interface SwedeTest {
    String featureDirectory() default "";

    String stepImplClasspath() default "";
}
