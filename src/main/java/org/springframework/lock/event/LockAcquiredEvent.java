package org.springframework.lock.event;

import org.springframework.context.ApplicationEvent;

/**
 * Application event which indicates that a lock is acquired.
 * 
 * @author matthias.augustin
 */
public class LockAcquiredEvent extends ApplicationEvent {

   private static final long serialVersionUID = 2398776765262324339L;

   private final Object      identifier;

   public LockAcquiredEvent(final String key, final Object identifier) {
      super(key);
      this.identifier = identifier;
   }

   public Object getIdentifier() {
      return identifier;
   }

   public String getKey() {
      return (String) getSource();
   }

}
