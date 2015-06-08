package org.springframework.lock.support;

import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;

public class DefaultLockSynchronizationManager extends AbstractLockSynchronizationManager {

   @Override
   public void release(final LockInstance lockInstance) throws LockException {
      doRelease(lockInstance);
   }

}
