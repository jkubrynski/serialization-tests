package com.kubrynski.poc.serial;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import java.io.*;

/**
 * @author Jakub Kubrynski <jkubrynski@gmail.com>
 * @since 29.07.12
 */
public class ObjectWriteTest {

  public static final String FILE_NAME = "out.test";
  public static final int MAX_BUFFER_SIZE = 1024;
  public static final String FILE_MODE_RW = "rw";
  public static final String FILE_MODE_R = "r";

/*
  public static SerializationTest buildKryoSerialization() {
    return new KryoSerialization();
  }
*/

  public static SerializationTest buildKryo2Serialization() {
    return new Kryo2Serialization();
  }

  public static SerializationTest buildDirectSerialization() {
    return new DirectSerialization();
  }

  public static SerializationTest buildStandardSerializationRaf() {
    return new StandardSerializationRaf();
  }

  public static SerializationTest buildStandardSerialization() {
    return new StandardSerialization();
  }

  private static class StandardSerialization implements SerializationTest {
    public void testWriteBuffered(TestObject test, String fileName) throws IOException {
      ObjectOutputStream objectOutputStream = null;
      try {
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
        objectOutputStream.writeObject(test);
      } finally {
        if (objectOutputStream != null) {
          objectOutputStream.close();
        }
      }
    }

    public TestObject testReadBuffered(String fileName) throws IOException, ClassNotFoundException {
      ObjectInputStream objectInputStream = null;
      try {
        FileInputStream fileInputStream = new FileInputStream(fileName);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        objectInputStream = new ObjectInputStream(bufferedInputStream);
        return (TestObject) objectInputStream.readObject();
      } finally {
        if (objectInputStream != null) {
          objectInputStream.close();
        }
      }
    }
  }

  private static class StandardSerializationRaf implements SerializationTest {
    public void testWriteBuffered(TestObject test, String fileName) throws IOException {
      ObjectOutputStream objectOutputStream = null;
      try {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, FILE_MODE_RW);
        FileOutputStream fileOutputStream = new FileOutputStream(randomAccessFile.getFD());
        objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(test);
      } finally {
        if (objectOutputStream != null) {
          objectOutputStream.close();
        }
      }
    }

    public TestObject testReadBuffered(String fileName) throws IOException, ClassNotFoundException {
      ObjectInputStream objectInputStream = null;
      try {
        RandomAccessFile randomAccessFile = new RandomAccessFile(fileName, FILE_MODE_R);
        FileInputStream fileInputStream = new FileInputStream(randomAccessFile.getFD());
        objectInputStream = new ObjectInputStream(fileInputStream);
        return (TestObject) objectInputStream.readObject();
      } finally {
        if (objectInputStream != null) {
          objectInputStream.close();
        }
      }
    }
  }

  private static class DirectSerialization implements SerializationTest {
    public void testWriteBuffered(TestObject test, String fileName) throws IOException {
      RandomAccessFile raf = null;
      try {
        MemoryBuffer memoryBuffer = new MemoryBuffer(MAX_BUFFER_SIZE);
        raf = new RandomAccessFile(fileName, FILE_MODE_RW);
        test.write(memoryBuffer);
        raf.write(memoryBuffer.getBuffer());
      } finally {
        if (raf != null) {
          raf.close();
        }
      }
    }

    public TestObject testReadBuffered(String fileName) throws IOException {
      RandomAccessFile raf = null;
      try {
        raf = new RandomAccessFile(fileName, FILE_MODE_R);
        MemoryBuffer unsafeBuffer = new MemoryBuffer((int) raf.length());
        raf.read(unsafeBuffer.getBuffer());
        return TestObject.read(unsafeBuffer);
      } finally {
        if (raf != null) {
          raf.close();
        }
      }
    }
  }

/*
  private static class KryoSerialization implements SerializationTest {

    private static Kryo kryo = new Kryo();

    static {
      kryo.register(long[].class);
      kryo.register(TestObject.class);
    }

    public void testWriteBuffered(TestObject test, String fileName) throws IOException {
      RandomAccessFile raf = null;
      try {
        raf = new RandomAccessFile(fileName, FILE_MODE_RW);
        ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
        kryo.writeObject(byteBuffer, test);
        raf.write(byteBuffer.array());
      } finally {
        if (raf != null) {
          raf.close();
        }
      }
    }

    public TestObject testReadBuffered(String fileName) throws IOException {
      RandomAccessFile raf = null;
      try {
        raf = new RandomAccessFile(fileName, FILE_MODE_R);
        ByteBuffer byteBuffer = ByteBuffer.allocate(MAX_BUFFER_SIZE);
        raf.read(byteBuffer.array());
        return kryo.readObject(byteBuffer, TestObject.class);
      } finally {
        if (raf != null) {
          raf.close();
        }
      }
    }
  }
*/

  private static class Kryo2Serialization implements SerializationTest {

    private static Kryo kryo = new Kryo();

    public void testWriteBuffered(TestObject test, String fileName) throws IOException {
      Output output = null;
      try {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        output = new Output(new FileOutputStream(raf.getFD()), MAX_BUFFER_SIZE);
        kryo.writeObject(output, test);
      } finally {
        if (output != null) {
          output.close();
        }
      }
    }

    public TestObject testReadBuffered(String fileName) throws IOException {
      Input input = null;
      try {
        RandomAccessFile raf = new RandomAccessFile(fileName, "r");
        input = new Input(new FileInputStream(raf.getFD()), MAX_BUFFER_SIZE);
        return kryo.readObject(input, TestObject.class);
      } finally {
        if (input != null) {
          input.close();
        }
      }

    }
  }


}
