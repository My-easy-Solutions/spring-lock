package org.springframework.lock.annotation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lock.LockService;
import org.springframework.lock.interceptor.LockInterceptor;
import org.springframework.lock.local.LocalLockService;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

@Configuration
public class ProxyLockConfiguration implements ImportAware {

   private AnnotationAttributes enableLock;
   private LockService          lockService = new LocalLockService();

   @Bean
   @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
   public BeanFactoryLockAttributeSourceAdvisor lockAdvisor() {
      Assert.notNull(enableLock, "@EnableLock annotation metadata was not injected");
      BeanFactoryLockAttributeSourceAdvisor advisor = new BeanFactoryLockAttributeSourceAdvisor();
      advisor.setOrder(enableLock.<Integer> getNumber("order"));
      advisor.setLockAttributeSource(lockAttributeSource());
      advisor.setAdvice(lockInterceptor());

      return advisor;
   }

   @Bean
   @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
   public LockAttributeSource lockAttributeSource() {
      return new AnnotationLockAttributeSource();
   }

   @Bean
   @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
   public LockInterceptor lockInterceptor() {
      LockInterceptor interceptor = new LockInterceptor();
      interceptor.setLockService(lockService);
      interceptor.setLockAttributeSource(lockAttributeSource());
      interceptor.setSessionTransacted(enableLock.getBoolean("sessionTransacted"));

      return interceptor;
   }

   @Override
   public void setImportMetadata(final AnnotationMetadata importMetadata) {
      enableLock = AnnotationAttributes.fromMap(
                                                importMetadata.getAnnotationAttributes(EnableLock.class.getName(), false));
      if (enableLock == null) {
         throw new IllegalArgumentException(
                                            "@EnableLock is not present on importing class "
                                                     + importMetadata.getClassName());
      }
   }

   /**
    * Collect any {@link LockService} beans through autowiring.
    */
   @Autowired(required = false)
   void setLockService(final Collection<LockService> lockServices) {
      if (CollectionUtils.isEmpty(lockServices)) {
         return;
      }
      if (lockServices.size() > 1) {
         throw new IllegalStateException("Only one LockService may exist");
      }

      lockService = lockServices.iterator().next();
   }
}
