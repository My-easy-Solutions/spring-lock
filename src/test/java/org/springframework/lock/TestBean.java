package org.springframework.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lock.annotation.Identifier;
import org.springframework.lock.annotation.Lock;

public class TestBean {

   private Logger logger = LoggerFactory.getLogger(getClass());
   private Object object = new Object();

   @Lock("LockKey")
   public void doSomething() {
      wakeMeUpLater();
   }

   @Lock("LockWithParameter")
   public void doSomething(@Identifier final String param) {
      wakeMeUpLater();
   }

   public void wakeUp() {
      synchronized (object) {
         logger.info("WakeUp threads");
         object.notifyAll();
      }
   }

   private void wakeMeUpLater() {
      try {
         synchronized (object) {
            logger.info("doSomething called. Wake UP me later");
            object.wait();
         }
      } catch (InterruptedException e) {
         e.printStackTrace();
      }
      logger.info("You waked me up");
   }

}
