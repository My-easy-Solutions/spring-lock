package org.springframework.lock.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author matthias.augustin
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

   /** Maximum timeout to wait for lock in milleseconds. */
   long timeout() default -1L;

   /** Lock identifier. */
   String value();
}
