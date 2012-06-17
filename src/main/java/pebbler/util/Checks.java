package pebbler.util;

import java.util.Collection;

public class Checks {

  private Checks() {}

  public static <T> T checkNotNull(T ref) {
    if (ref == null) {
      throw new NullPointerException();
    }
    return ref;
  }

  public static <T> T checkNotNull(T ref, String message) {
    if (ref == null) {
      throw new NullPointerException("Reference null: " + message);
    }
    return ref;
  }

  public static <T extends Collection> T checkNotEmpty(
      T ref,
      String message) {
    checkNotNull(ref, message);
    if (ref.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
    return ref;
  }

  public static <T extends Collection> T checkNotEmpty(T ref) {
    checkNotNull(ref);
    if (ref.isEmpty()) {
      throw new IllegalArgumentException();
    }
    return ref;
  }
}
