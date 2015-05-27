package org.springframework.lock.interceptor;

import java.io.Serializable;
import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lock.annotation.LockAttributeSource;
import org.springframework.util.ObjectUtils;

public abstract class LockAttributeSourcePointcut extends StaticMethodMatcherPointcut implements Serializable {

   private static final long serialVersionUID = 1L;

   @Override
   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      }
      if (!(other instanceof LockAttributeSourcePointcut)) {
         return false;
      }
      LockAttributeSourcePointcut otherPc = (LockAttributeSourcePointcut) other;
      return ObjectUtils.nullSafeEquals(getLockAttributeSource(), otherPc.getLockAttributeSource());
   }

   public abstract LockAttributeSource getLockAttributeSource();

   @Override
   public int hashCode() {
      return LockAttributeSourcePointcut.class.hashCode();
   }

   @Override
   public boolean matches(final Method method, final Class<?> targetClass) {
      LockAttributeSource las = getLockAttributeSource();
      return las == null
             || las.getLockAttribute(method, targetClass) != null;
   }

   @Override
   public String toString() {
      return getClass().getName()
               + ": " + getLockAttributeSource();
   }
}
