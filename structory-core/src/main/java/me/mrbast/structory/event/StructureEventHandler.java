package me.mrbast.structory.event;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface StructureEventHandler {

    public StructureEventPriority priority() default StructureEventPriority.NORMAL;
}
