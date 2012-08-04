/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kubrynski.poc.serial;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;

import java.io.IOException;

/**
 * @author Jakub Kubrynski <jkubrynski@gmail.com>
 * @since 29.07.12
 */
public class ObjectWriterBenchmark extends SimpleBenchmark {

  @Param
  private SerializationType serializationType;

  private SerializationTest serializationTest;

  private TestObject testObject;

  @Override
  protected void setUp() throws Exception {
    testObject = new TestObject();
    testObject.setLongVariable(1l);
    testObject.setLongArray(new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
    testObject.setStringObject("This old man, he played one, He played knick-knack on my thumb");
    serializationTest = serializationType.create();
  }

  public void timeSerializationFullTrip(int reps) throws IOException, ClassNotFoundException {
    for (int i = 0; i < reps; i++) {
      serializationTest.testWriteBuffered(testObject, ObjectWriteTest.FILE_NAME);
      serializationTest.testReadBuffered(ObjectWriteTest.FILE_NAME);
    }
  }

  public enum SerializationType {
    STANDARD {
      @Override
      SerializationTest create() {
        return ObjectWriteTest.buildStandardSerialization();
      }
    },

    STANDARD_RAF {
      @Override
      SerializationTest create() {
        return ObjectWriteTest.buildStandardSerializationRaf();
      }
    },

    KRYO {
      @Override
      SerializationTest create() {
        return ObjectWriteTest.buildKryoSerialization();
      }
    },

    UNSAFE_MEMORY {
      @Override
      SerializationTest create() {
        return ObjectWriteTest.buildDirectSerialization();
      }
    };

    abstract SerializationTest create();
  }


  public static void main(String[] args) throws Exception {
    Runner.main(ObjectWriterBenchmark.class, args);
  }
}