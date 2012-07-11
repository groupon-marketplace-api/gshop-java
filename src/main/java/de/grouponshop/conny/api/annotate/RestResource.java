package de.grouponshop.conny.api.annotate;

import java.lang.annotation.Retention; 
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RestResource {
    
    String path();
    String filters() default "";
}
