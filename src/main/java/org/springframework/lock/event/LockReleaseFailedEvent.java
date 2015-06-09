package org.springframework.lock.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.lock.LockException;

/**
 * Application event which indicates that acquire a lock is failed.
 * 
 * @author matthias.augustin
 */
public class LockReleaseFailedEvent extends ApplicationEvent {

   private static final long   serialVersionUID = 2398776765262324339L;

   private final Object        identifier;
   private final LockException exception;

   public LockReleaseFailedEvent(final String key, final Object identifier, final LockException exception) {
      super(key);
      this.identifier = identifier;
      this.exception = exception;
   }

   public LockException getException() {
      return exception;
   }

   public Object getIdentifier() {
      return identifier;
   }

   public String getKey() {
      return (String) getSource();
   }

}
