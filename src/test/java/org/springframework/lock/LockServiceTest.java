package org.springframework.lock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = LockServiceTestConfiguration.class)
public class LockServiceTest {

   @Autowired
   private TestBean testBean;

   @Test
   public void locking() throws Exception {
      Runnable r = new Runnable() {

         @Override
         public void run() {
            testBean.doSomething();
         }
      };

      Thread t1 = new Thread(null, r, "T1");
      Thread t2 = new Thread(null, r, "T2");

      // Start first thread
      t1.start();
      assertTrue(t1.isAlive());
      Thread.sleep(100); // Thread t1 should be first

      // Start second thread
      t2.start();
      assertTrue(t2.isAlive());

      testBean.wakeUp();

      t1.join(100);
      t2.join(100);
      assertFalse(t1.isAlive());
      assertTrue(t2.isAlive());

      testBean.wakeUp();
      t2.join(100);
      assertFalse(t2.isAlive());
   }

   @Test
   public void lockingWithParameters() throws Exception {
      Runnable r = new Runnable() {

         @Override
         public void run() {
            testBean.doSomething(Thread.currentThread().getName());
         }
      };

      Thread t1 = new Thread(null, r, "T1");
      Thread t2 = new Thread(null, r, "T2");

      t1.start();
      t2.start();
      Thread.sleep(100);
      assertTrue(t1.isAlive());
      assertTrue(t2.isAlive());

      testBean.wakeUp();

      t1.join(100);
      t2.join(100);
      assertFalse(t1.isAlive());
      assertFalse(t2.isAlive());
   }

   @Test
   public void lockingWithSameParameter() throws Exception {
      Runnable r = new Runnable() {

         @Override
         public void run() {
            testBean.doSomething("X");
         }
      };

      Thread t1 = new Thread(null, r, "T1");
      Thread t2 = new Thread(null, r, "T2");

      // Start first thread
      t1.start();
      assertTrue(t1.isAlive());
      Thread.sleep(100); // Thread t1 should be first

      // Start second thread
      t2.start();
      assertTrue(t2.isAlive());

      testBean.wakeUp();

      t1.join(100);
      t2.join(100);
      assertFalse(t1.isAlive());
      assertTrue(t2.isAlive());

      testBean.wakeUp();
      t2.join(100);
      assertFalse(t2.isAlive());
   }
}
