package com.kubrynski.poc.serial;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class Util {
  private static final Unsafe THE_UNSAFE;

  static {
    try {
      Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
      theUnsafe.setAccessible(true);
      THE_UNSAFE = (Unsafe) theUnsafe.get(null);
    } catch (Exception e) {
      throw new RuntimeException("Unable to load unsafe", e);
    }
  }

  public static Unsafe getUnsafe() {
    return THE_UNSAFE;
  }
}
