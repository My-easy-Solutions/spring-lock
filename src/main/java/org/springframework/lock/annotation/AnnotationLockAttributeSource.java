package org.springframework.lock.annotation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

public class AnnotationLockAttributeSource implements LockAttributeSource {

   private static final LockAttribute       DEFAULT_LOCK_ATTRIBUTE = new DefaultLockAttribute(null, null, -1L);

   private Logger                           logger                 = LoggerFactory.getLogger(getClass());

   private final Map<Object, LockAttribute> attributeCache         = new ConcurrentHashMap<Object, LockAttribute>(1024);

   private LockAnnotationParser             lockAnnotationParser   = new LockAnnotationParser();

   @Override
   public LockAttribute getLockAttribute(final Method method, final Class<?> targetClass) {
      // First, see if we have a cached value.
      Object cacheKey = getCacheKey(method, targetClass);
      LockAttribute cached = attributeCache.get(cacheKey);

      if (cached != null) {
         if (cached == DEFAULT_LOCK_ATTRIBUTE) {
            return null;
         } else {
            return cached;
         }

      } else {
         // Resolve and cache lock attribute
         LockAttribute attribute = resolveAttribute(method, targetClass);
         if (null != attribute) {
            logger.debug("Lock attribute for {} is {}", cacheKey, attribute);
            attributeCache.put(cacheKey, attribute);
         } else {
            attributeCache.put(cacheKey, DEFAULT_LOCK_ATTRIBUTE);
         }

         return attribute;
      }

   }

   private Object getCacheKey(final Method method, final Class<?> targetClass) {
      return new DefaultCacheKey(method, targetClass);
   }

   private LockAttribute getLockAttribute(final AnnotatedElement element) {
      return lockAnnotationParser.parseLockAnnotation(element);
   }

   private LockAttribute resolveAttribute(final Method method, final Class<?> targetClass) {

      if (!Modifier.isPublic(method.getModifiers())) {
         // Only support public methods
         return null;
      }

      // Ignore CGLIB subclasses - introspect the actual user class.
      Class<?> userClass = ClassUtils.getUserClass(targetClass);
      // The method may be on an interface, but we need attributes from the target class.
      // If the target class is null, the method will be unchanged.
      Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
      // If we are dealing with method with generic parameters, find the original method.
      specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);

      LockAttribute attribute = getLockAttribute(specificMethod);
      if (null != attribute) {
         return attribute;
      }

      attribute = getLockAttribute(specificMethod.getDeclaringClass());

      if (null != attribute) {
         return attribute;
      }

      if (specificMethod != method) {
         // Fallback is to look at the original method.
         attribute = getLockAttribute(method);
         if (attribute != null) {
            return attribute;
         }
         // Last fallback is the class of the original method.
         return getLockAttribute(method.getDeclaringClass());
      }

      return null;
   }

   /**
    * Default cache key for the TransactionAttribute cache.
    */
   private static class DefaultCacheKey {

      private final Method   method;

      private final Class<?> targetClass;

      public DefaultCacheKey(final Method method, final Class<?> targetClass) {
         this.method = method;
         this.targetClass = targetClass;
      }

      @Override
      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         }
         if (!(other instanceof DefaultCacheKey)) {
            return false;
         }
         DefaultCacheKey otherKey = (DefaultCacheKey) other;
         return method.equals(otherKey.method)
                &&
                ObjectUtils.nullSafeEquals(targetClass, otherKey.targetClass);
      }

      @Override
      public int hashCode() {
         return method.hashCode()
                + (targetClass != null ? targetClass.hashCode() * 29
                                      : 0);
      }
   }
}
