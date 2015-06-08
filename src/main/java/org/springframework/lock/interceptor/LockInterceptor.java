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
import org.springframework.lock.annotation.LockAttribute;
import org.springframework.lock.annotation.LockAttributeSource;
import org.springframework.lock.support.LockSynchronizationManager;

public class LockInterceptor implements InitializingBean, MethodInterceptor, Serializable {

   private static final long          serialVersionUID = 1L;

   private static final Logger        LOGGER           = LoggerFactory.getLogger(LockInterceptor.class);

   private LockAttributeSource        lockAttributeSource;

   private LockSynchronizationManager synchronizationManager;

   @Override
   public void afterPropertiesSet() throws Exception {
      requireNonNull(synchronizationManager);
      requireNonNull(lockAttributeSource);
   }

   @Override
   public Object invoke(final MethodInvocation invocation) throws Throwable {

      Class<?> targetClass = invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis())
                                                         : null;

      LockAttribute lockAttribute = lockAttributeSource.getLockAttribute(invocation.getMethod(), targetClass);
      LockInstance lock = acquireLock(lockAttribute, invocation.getArguments());
      try {
         return invocation.proceed();
      } finally {
         releaseLock(lock);
      }

   }

   public void setLockAttributeSource(final LockAttributeSource lockAttributeSource) {
      this.lockAttributeSource = lockAttributeSource;
   }

   public void setSynchronizationManager(final LockSynchronizationManager synchronizationManager) {
      this.synchronizationManager = synchronizationManager;
   }

   private LockInstance acquireLock(final LockAttribute lockAttribute, final Object[] arguments) {
      LockInstance lock = null;
      if (null != lockAttribute) {
         lock = synchronizationManager.acquire(lockAttribute.getKey(), lockAttribute.getIdentifier(arguments),
                                               lockAttribute.getTimeout());
      }
      return lock;
   }

   private void releaseLock(final LockInstance lock) {
      if (null != lock) {
         try {
            synchronizationManager.release(lock);
         } catch (LockException e) {
            LOGGER.warn("Failed to release lock {}", lock, e);
         }
      }
   }

}
