package org.springframework.lock;

import java.io.Serializable;

public interface LockInstance extends Serializable {

   Object getIdentifier();

   String getKey();
}
