package org.springframework.lock;

import org.springframework.context.annotation.Bean;
import org.springframework.lock.annotation.EnableLock;

@EnableLock
public class LockServiceTestConfiguration {

   @Bean
   private TestBean testBean() {
      return new TestBean();
   }
}
