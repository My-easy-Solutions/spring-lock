package org.springframework.lock;

public class LockException extends RuntimeException {

   private static final long serialVersionUID = 1L;

   public LockException() {
      super();
   }

   public LockException(final String message) {
      super(message);
   }

   public LockException(final String message, final Throwable cause) {
      super(message, cause);
   }

   public LockException(final Throwable cause) {
      super(cause);
   }

}
