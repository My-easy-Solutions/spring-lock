package org.springframework.lock.support;

import static org.springframework.util.ObjectUtils.nullSafeEquals;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.NamedThreadLocal;
import org.springframework.lock.LockInstance;

public abstract class LockSynchronizationStore {

   private static final ThreadLocal<Set<LockInstance>> LOCKS = new NamedThreadLocal<Set<LockInstance>>("current locks");

   public static void add(final LockInstance lockInstance) {
      Set<LockInstance> locks = LOCKS.get();
      if (null == locks) {
         locks = new HashSet<LockInstance>();
         LOCKS.set(locks);
      }

      locks.add(lockInstance);
   }

   public static LockInstance get(final String key, final Object identifier) {
      Set<LockInstance> locks = LOCKS.get();
      if (locks == null) {
         return null;
      }

      LockInstance instance = null;
      for (LockInstance lockInstance : locks) {
         if (nullSafeEquals(lockInstance.getKey(), key)
             && nullSafeEquals(lockInstance.getIdentifier(), identifier)) {
            instance = lockInstance;
            break;
         }
      }

      return instance;
   }

   public static void remove(final LockInstance lockInstance) {
      Set<LockInstance> locks = LOCKS.get();
      if (null != locks) {
         locks.remove(lockInstance);
      }
   }

   private LockSynchronizationStore() {
      super();
   }
}
