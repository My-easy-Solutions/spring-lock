package org.springframework.lock.annotation;

import org.springframework.cglib.beans.BeanMap;

public class IdentifierResolver {

   private final String property;
   private final int    index;

   public IdentifierResolver(final String property, final int index) {
      this.property = property;
      this.index = index;
   }

   public Object getIdentifier(final Object[] parameters) {
      if ("".equals(property)) {
         return parameters[index];
      }
      return BeanMap.create(parameters[index]).get(property);
   }
}
