package org.springframework.lock;

import org.springframework.context.ApplicationListener;
import org.springframework.lock.event.LockReleasedEvent;

public class LockReleasedListener implements ApplicationListener<LockReleasedEvent> {

   @Override
   public void onApplicationEvent(final LockReleasedEvent event) {
      System.out.println(event);
   }

}
