package org.springframework.lock.annotation;

import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.context.annotation.AutoProxyRegistrar;

public class LockConfigurationSelector extends AdviceModeImportSelector<EnableLock> {

   @Override
   protected String[] selectImports(final AdviceMode adviceMode) {
      switch (adviceMode) {
         case PROXY:
            return new String[] { AutoProxyRegistrar.class.getName(), ProxyLockConfiguration.class.getName() };
         case ASPECTJ:
            throw new UnsupportedOperationException("Advice Mode AJPECTJ is not supported yet");
         default:
            return null;
      }
   }

}
