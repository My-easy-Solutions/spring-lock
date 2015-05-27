package org.springframework.lock;

public class DefaultLockInstance implements LockInstance {

   private static final long serialVersionUID = -6163953506521564197L;

   private final Object      identifier;
   private final String      key;

   public DefaultLockInstance(final String key, final Object identifier) {
      super();
      this.key = key;
      this.identifier = identifier;
   }

   @Override
   public boolean equals(final Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      DefaultLockInstance other = (DefaultLockInstance) obj;
      if (identifier == null) {
         if (other.identifier != null) {
            return false;
         }
      } else if (!identifier.equals(other.identifier)) {
         return false;
      }
      if (key == null) {
         if (other.key != null) {
            return false;
         }
      } else if (!key.equals(other.key)) {
         return false;
      }
      return true;
   }

   @Override
   public Object getIdentifier() {
      return identifier;
   }

   @Override
   public String getKey() {
      return key;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime
               * result + (identifier == null ? 0
                                             : identifier.hashCode());
      result = prime
               * result + (key == null ? 0
                                      : key.hashCode());
      return result;
   }

   @Override
   public String toString() {
      return new StringBuilder().append("DefaultLockInstance [").append(identifier).append("@").append(key).append("]")
               .toString();
   }

}
