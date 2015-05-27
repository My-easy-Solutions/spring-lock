package org.springframework.lock.annotation;

import java.lang.reflect.Method;

public interface LockAttributeSource {

   LockAttribute getLockAttribute(Method method, Class<?> targetClass);

}
