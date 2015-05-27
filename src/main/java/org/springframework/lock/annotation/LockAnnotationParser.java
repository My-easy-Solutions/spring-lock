package org.springframework.lock.annotation;

import java.lang.reflect.AnnotatedElement;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

public class LockAnnotationParser {

   public LockAttribute parseLockAnnotation(final AnnotatedElement element) {
      AnnotationAttributes ann = AnnotatedElementUtils.getAnnotationAttributes(element, Lock.class.getName());
      if (ann != null) {
         return parseTransactionAnnotation(ann);
      }
      else {
         return null;
      }
   }

   protected LockAttribute parseTransactionAnnotation(final AnnotationAttributes attributes) {
      String key = attributes.getString("value");

      return new DefaultLockAttribute(key);
   }

}
