package org.example.pccwtest.security.annotation;

import java.lang.annotation.*;

/**
 * Custom annotation to indicate that a method or type should be accessible
 * without authentication.
 * <p>
 * This annotation is typically used to mark endpoints or classes that
 * should be accessible to all users, regardless of authentication status.
 * </p>
 */
@Target( { ElementType.METHOD, ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Anonymous {
}