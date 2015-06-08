package org.springframework.lock.annotation;

public class DefaultLockAttribute implements LockAttribute {

   private final String             key;
   private final IdentifierResolver identifierResolver;
   private final long               timeout;

   public DefaultLockAttribute(final String key, final IdentifierResolver identifierResolver, final long timeout) {
      this.key = key;
      this.identifierResolver = identifierResolver;
      this.timeout = timeout;
   }

   @Override
   public Object getIdentifier(final Object[] arguments) {
      return identifierResolver == null ? null
                                       : identifierResolver.getIdentifier(arguments);
   }

   @Override
   public String getKey() {
      return key;
   }

   @Override
   public long getTimeout() {
      return timeout;
   }

}
