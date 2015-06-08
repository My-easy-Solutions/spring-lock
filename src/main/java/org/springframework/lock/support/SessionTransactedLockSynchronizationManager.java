package org.springframework.lock.support;

import java.util.HashSet;
import java.util.Set;

import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class SessionTransactedLockSynchronizationManager extends AbstractLockSynchronizationManager {

   @Override
   public void release(final LockInstance lockInstance) throws LockException {

      if (TransactionSynchronizationManager.isSynchronizationActive()) {
         releaseWithTransaction(lockInstance);
      } else {
         doRelease(lockInstance);
      }

   }

   private void releaseWithTransaction(final LockInstance lockInstance) {
      TransactionLockSynchronization synchronization = null;
      for (TransactionSynchronization transactionSynchronization : TransactionSynchronizationManager
               .getSynchronizations()) {
         if (transactionSynchronization instanceof TransactionLockSynchronization) {
            synchronization = (TransactionLockSynchronization) transactionSynchronization;
         }
      }

      if (null == synchronization) {
         synchronization = new TransactionLockSynchronization(this);
         TransactionSynchronizationManager.registerSynchronization(synchronization);
      }

      synchronization.add(lockInstance);
   }

   private static class TransactionLockSynchronization extends TransactionSynchronizationAdapter {

      private final Set<LockInstance>                  instances = new HashSet<LockInstance>();
      private final AbstractLockSynchronizationManager manager;

      public TransactionLockSynchronization(final AbstractLockSynchronizationManager manager) {
         this.manager = manager;
      }

      public void add(final LockInstance lockInstance) {
         instances.add(lockInstance);
      }

      @Override
      public void afterCompletion(final int status) {
         for (LockInstance lockInstance : instances) {
            manager.doRelease(lockInstance);
         }
      }
   }
}
