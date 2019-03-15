package edu.grenoble.em.bourji;

import javax.ws.rs.NameBinding;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  Annotation declaration to be used for Name binding: This would bind methods/classes to a receptor that is decorated with the same annotation.
 *  This will be used to annotate resources that require user authorization.
 *  Target: Where the annotation can be used: Type = class
 *  Retention: how long the annotation will be retained (http://www.java2novice.com/java-annotations/retention-policy/)
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(value = RetentionPolicy.RUNTIME)
@NameBinding
public @interface Authenticate {
}