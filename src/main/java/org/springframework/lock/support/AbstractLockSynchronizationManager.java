package org.springframework.lock.support;

import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;
import org.springframework.lock.LockService;
import org.springframework.lock.event.LockEventPublisher;

public abstract class AbstractLockSynchronizationManager implements LockSynchronizationManager {

   private LockService        lockService;
   private LockEventPublisher lockEventPublisher;

   @Override
   public LockInstance acquire(final String key, final Object identifier, final long timeout) throws LockException {
      try {
         LockInstance lockInstance = LockSynchronizationStore.get(key, identifier);
         if (null == lockInstance) {
            lockInstance = getLockService().acquire(key, identifier, timeout);
            LockSynchronizationStore.add(lockInstance);
         }

         lockEventPublisher.acquired(key, identifier);

         return lockInstance;
      } catch (LockException e) {
         lockEventPublisher.acquireFailed(key, identifier, e);
         throw e;
      }
   }

   public LockService getLockService() {
      return lockService;
   }

   @Override
   public boolean hasLock(final String key, final Object identifier) {
      return LockSynchronizationStore.get(key, identifier) != null;
   }

   @Override
   public abstract void release(final LockInstance lockInstance) throws LockException;

   public void setLockEventPublisher(final LockEventPublisher lockEventPublisher) {
      this.lockEventPublisher = lockEventPublisher;
   }

   public void setLockService(final LockService lockService) {
      this.lockService = lockService;
   }

   protected void doRelease(final LockInstance lockInstance) {
      try {
         getLockService().release(lockInstance);
         LockSynchronizationStore.remove(lockInstance);
         lockEventPublisher.released(lockInstance.getKey(), lockInstance.getIdentifier());
      } catch (LockException e) {
         lockEventPublisher.releaseFailed(lockInstance.getKey(), lockInstance.getIdentifier(), e);
      }
   }
}
