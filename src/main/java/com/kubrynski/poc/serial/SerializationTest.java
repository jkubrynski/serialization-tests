package com.kubrynski.poc.serial;

import java.io.IOException;

/**
 * @author Jakub Kubrynski <jkubrynski@gmail.com>
 * @since 29.07.12
 */
public interface SerializationTest {
  void testWriteBuffered(TestObject test, String fileName) throws IOException;

  TestObject testReadBuffered(String fileName) throws IOException, ClassNotFoundException;
}
