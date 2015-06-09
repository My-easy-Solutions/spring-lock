package org.springframework.lock.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.lock.LockException;

public class LockEventPublisher implements ApplicationEventPublisherAware {

   private ApplicationEventPublisher applicationEventPublisher;

   public void acquired(final String key, final Object identifier) {
      applicationEventPublisher.publishEvent(new LockAcquiredEvent(key, identifier));
   }

   public void acquireFailed(final String key, final Object identifier, final LockException e) {
      applicationEventPublisher.publishEvent(new LockAcquireFailedEvent(key, identifier, e));
   }

   public void released(final String key, final Object identifier) {
      applicationEventPublisher.publishEvent(new LockReleasedEvent(key, identifier));
   }

   public void releaseFailed(final String key, final Object identifier, final LockException e) {
      applicationEventPublisher.publishEvent(new LockReleaseFailedEvent(key, identifier, e));
   }

   @Override
   public void setApplicationEventPublisher(final ApplicationEventPublisher applicationEventPublisher) {
      this.applicationEventPublisher = applicationEventPublisher;
   }

}
