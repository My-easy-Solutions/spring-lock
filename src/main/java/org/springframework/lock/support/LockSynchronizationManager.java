package org.springframework.lock.support;

import org.springframework.lock.LockException;
import org.springframework.lock.LockInstance;

public interface LockSynchronizationManager {

   LockInstance acquire(String key, Object identifier, long timeout) throws LockException;

   boolean hasLock(String key, Object identifier);

   void release(LockInstance lock) throws LockException;
}
