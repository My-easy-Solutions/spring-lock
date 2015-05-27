package org.springframework.lock.interceptor;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;
import org.springframework.lock.LockService;
import org.springframework.lock.annotation.LockAttribute;
import org.springframework.lock.annotation.LockAttributeSource;

public class LockInterceptor implements InitializingBean, MethodInterceptor, Serializable {

   private static final long   serialVersionUID = 1L;

   private static final Logger LOGGER           = LoggerFactory.getLogger(LockInterceptor.class);

   private LockService         lockService;

   private LockAttributeSource lockAttributeSource;

   private boolean             sessionTransacted;

   @Override
   public void afterPropertiesSet() throws Exception {
      requireNonNull(lockService);
      requireNonNull(lockAttributeSource);
   }

   @Override
   public Object invoke(final MethodInvocation invocation) throws Throwable {

      Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis())
                                                          : null;

      LockAttribute lockAttribute = lockAttributeSource.getLockAttribute(invocation.getMethod(), targetClass);
      LockInstance lock = acquireLock(lockAttribute);
      try {
         return invocation.proceed();
      } finally {
         releaseLock(lock);
      }

   }

   public void setLockAttributeSource(final LockAttributeSource lockAttributeSource) {
      this.lockAttributeSource = lockAttributeSource;
   }

   public void setLockService(final LockService lockService) {
      this.lockService = lockService;
   }

   public void setSessionTransacted(final boolean sessionTransacted) {
      this.sessionTransacted = sessionTransacted;
   }

   private LockInstance acquireLock(final LockAttribute lockAttribute) {
      LockInstance lock = null;
      if (null != lockAttribute) {
         lock = lockService.acquire(lockAttribute.getKey(), null, -1L);
      }
      return lock;
   }

   private void releaseLock(final LockInstance lock) {
      if (null != lock) {
         try {
            lockService.release(lock);
         } catch (LockException e) {
            LOGGER.warn("Failed to release lock {}", lock, e);
         }
      }
   }

}
