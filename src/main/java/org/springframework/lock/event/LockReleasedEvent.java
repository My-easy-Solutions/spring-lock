package org.springframework.lock.event;

import org.springframework.context.ApplicationEvent;

/**
 * Application event which indicates that a lock is released.
 * 
 * @author matthias.augustin
 */
public class LockReleasedEvent extends ApplicationEvent {

   private static final long serialVersionUID = 2398776765262324339L;

   private final Object      identifier;

   public LockReleasedEvent(final String key, final Object identifier) {
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
