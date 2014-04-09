package org.jboss.demos;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author lgao
 *
 * Annotation on a method, to make it as a demo.
 * 
 * The declaring class must have a non-argument constructor.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Demo {

	/**
	 * Name of the demo
	 */
	String name();
	
	/**
	 * description of the demo
	 */
	String description() default "";
	
}
