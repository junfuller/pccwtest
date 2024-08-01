package org.example.pccwtest.security;

import java.lang.annotation.*;

@Target( { ElementType.METHOD, ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
@Documented
public @interface Anonymous {
}