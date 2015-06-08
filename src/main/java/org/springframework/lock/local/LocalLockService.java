package org.springframework.lock.local;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lock.DefaultLockInstance;
import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;
import org.springframework.lock.LockService;

/**
 * Simple class for handling locks local in the jvm.
 * 
 * @author matthias.augustin
 *
 */
public class LocalLockService implements LockService {

   private Logger                              logger = LoggerFactory.getLogger(getClass());

   private Map<String, Map<Object, Semaphore>> locks  = new HashMap<String, Map<Object, Semaphore>>();

   @Override
   public LockInstance acquire(final String key, final Object identifier, final long timeout) throws LockException {
      logger.debug("Acquire local lock {} - {}", key, identifier);

      Semaphore semaphore = getSemaphore(key, identifier);

      try {
         boolean acquired;
         if (timeout < 0) {
            semaphore.acquire();
            acquired = true;
         } else {
            acquired = semaphore.tryAcquire(timeout < 0 ? Long.MAX_VALUE
                                                        : timeout, TimeUnit.MILLISECONDS);
         }

         if (acquired) {
            return new DefaultLockInstance(key, identifier);
         } else {
            throw new LockException("Could not acquire lock "
                                    + key + "@" + identifier + " in " + timeout + "ms");
         }
      } catch (InterruptedException e) {
         throw new LockException();
      }
   }

   @Override
   public void release(final LockInstance lock) throws LockException {
      Objects.requireNonNull(lock);
      logger.debug("Release lock {}", lock);
      Semaphore semaphore = getSemaphore(lock.getKey(), lock.getIdentifier());
      if (null != semaphore) {
         semaphore.release();
      }
   }

   private Semaphore getSemaphore(final String key, final Object identifier) {
      Semaphore semaphore;
      synchronized (locks) {
         Map<Object, Semaphore> semaphores = locks.get(key);
         if (semaphores == null) {
            semaphores = new HashMap<Object, Semaphore>();
            locks.put(key, semaphores);
         }

         semaphore = semaphores.get(identifier);
         if (null == semaphore) {
            semaphore = new Semaphore(1, true);
            semaphores.put(identifier, semaphore);
         }
      }
      return semaphore;
   }

}
