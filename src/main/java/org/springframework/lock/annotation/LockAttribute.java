package org.springframework.lock.annotation;

public interface LockAttribute {

   Object getIdentifier(Object[] arguments);

   String getKey();

   long getTimeout();
}
