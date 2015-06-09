package org.springframework.lock;

import org.springframework.context.ApplicationListener;
import org.springframework.lock.event.LockAcquiredEvent;

public class LockAcquiredListener implements ApplicationListener<LockAcquiredEvent> {

   @Override
   public void onApplicationEvent(final LockAcquiredEvent event) {
      System.out.println(event);
   }

}
