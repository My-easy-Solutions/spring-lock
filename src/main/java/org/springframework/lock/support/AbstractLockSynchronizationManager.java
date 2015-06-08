package org.springframework.lock.support;

import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;
import org.springframework.lock.LockService;

public abstract class AbstractLockSynchronizationManager implements LockSynchronizationManager {

   private LockService lockService;

   @Override
   public LockInstance acquire(final String key, final Object identifier, final long timeout) throws LockException {
      LockInstance lockInstance = LockSynchronizationStore.get(key, identifier);
      if (null == lockInstance) {
         lockInstance = getLockService().acquire(key, identifier, timeout);
         LockSynchronizationStore.add(lockInstance);
      }

      return lockInstance;
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

   public void setLockService(final LockService lockService) {
      this.lockService = lockService;
   }

   protected void doRelease(final LockInstance lockInstance) {
      getLockService().release(lockInstance);
      LockSynchronizationStore.remove(lockInstance);
   }
}
