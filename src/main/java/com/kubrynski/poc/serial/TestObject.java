package com.kubrynski.poc.serial;

import java.io.*;

/**
 * @author Jakub Kubrynski <jkubrynski@gmail.com>
 * @since 29.07.12
 */
public class TestObject implements Serializable {

  private long longVariable;
  private long[] longArray;
  private String stringObject;
  private String secondStringObject;

  public void setLongArray(long[] longArray) {
    this.longArray = longArray;
  }

  public long[] getLongArray() {
    return longArray;
  }

  public void setLongVariable(long longVariable) {
    this.longVariable = longVariable;
  }

  public long getLongVariable() {
    return longVariable;
  }

  public String getStringObject() {
    return stringObject;
  }

  public void setStringObject(String stringObject) {
    this.stringObject = stringObject;
  }

  public void write(MemoryBuffer unsafeBuffer) {
    unsafeBuffer.putLong(longVariable);
    unsafeBuffer.putLongArray(longArray);
    // we support nulls :)
    boolean objectExists = stringObject != null;
    unsafeBuffer.putBoolean(objectExists);
    if (objectExists) {
      unsafeBuffer.putCharArray(stringObject.toCharArray());
    }
    objectExists = secondStringObject != null;
    unsafeBuffer.putBoolean(objectExists);
    if (objectExists) {
      unsafeBuffer.putCharArray(secondStringObject.toCharArray());
    }
  }

  public static TestObject read(MemoryBuffer unsafeBuffer) {
    final TestObject result = new TestObject();
    result.longVariable = unsafeBuffer.getLong();
    result.longArray = unsafeBuffer.getLongArray();
    boolean objectExists = unsafeBuffer.getBoolean();
    if (objectExists) {
      result.stringObject = String.valueOf(unsafeBuffer.getCharArray());
    }
    objectExists = unsafeBuffer.getBoolean();
    if (objectExists) {
      result.secondStringObject = String.valueOf(unsafeBuffer.getCharArray());
    }
    return result;
  }

}
