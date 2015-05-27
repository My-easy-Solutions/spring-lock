package org.springframework.lock.annotation;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lock.interceptor.LockAttributeSourcePointcut;

@SuppressWarnings("serial")
public class BeanFactoryLockAttributeSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

   private LockAttributeSource         lockAttributeSource;

   private LockAttributeSourcePointcut pointcut = new LockAttributeSourcePointcut() {

                                                   @Override
                                                   public LockAttributeSource getLockAttributeSource() {
                                                      return lockAttributeSource;
                                                   }
                                                };

   @Override
   public Pointcut getPointcut() {
      return pointcut;
   }

   public void setClassFilter(final ClassFilter classFilter) {
      pointcut.setClassFilter(classFilter);
   }

   public void setLockAttributeSource(final LockAttributeSource lockAttributeSource) {
      this.lockAttributeSource = lockAttributeSource;
   }

}
