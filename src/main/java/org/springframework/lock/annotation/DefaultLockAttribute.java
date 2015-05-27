package org.springframework.lock.annotation;

public class DefaultLockAttribute implements LockAttribute {

   private String key;

   public DefaultLockAttribute(final String key) {
      this.key = key;
   }

   @Override
   public Object getIdentifier(final Object[] arguments) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public String getKey() {
      return key;
   }

   @Override
   public long getTimeout() {
      // TODO Auto-generated method stub
      return 0;
   }

}
