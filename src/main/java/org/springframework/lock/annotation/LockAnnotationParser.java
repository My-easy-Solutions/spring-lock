package org.springframework.lock.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

public class LockAnnotationParser {

   public LockAttribute parseLockAnnotation(final AnnotatedElement element) {
      AnnotationAttributes ann = AnnotatedElementUtils.getAnnotationAttributes(element, Lock.class.getName());
      if (ann != null) {
         final AnnotationAttributes attributes = ann;
         String key = attributes.getString("value");
         Long timeout = attributes.getNumber("timeout");
         IdentifierResolver identifierResolver = identifierResolver(element);

         return new DefaultLockAttribute(key, identifierResolver, timeout);
      }
      else {
         return null;
      }
   }

   private IdentifierResolver identifierResolver(final AnnotatedElement element) {
      if (element instanceof Method) {
         Method method = (Method) element;
         Annotation[][] parameterAnnotations = method.getParameterAnnotations();

         for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] annotations = parameterAnnotations[i];
            for (Annotation annotation : annotations) {
               if (annotation instanceof Identifier) {
                  Identifier identifier = (Identifier) annotation;
                  System.out.println("value: "
                           + identifier.value());

                  return new IdentifierResolver(identifier.value(), i);
               }
            }
         }

      }

      return null;
   }

}
