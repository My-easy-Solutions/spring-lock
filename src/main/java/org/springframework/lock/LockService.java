package org.springframework.lock;

public interface LockService {

   LockInstance acquire(String key, Object identifier, long timeout) throws LockException;

   void release(LockInstance lock) throws LockException;
}
