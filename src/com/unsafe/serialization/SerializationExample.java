package com.unsafe.serialization;

import sun.misc.Unsafe;

import java.util.Arrays;

public class SerializationExample {

    public static final int REPETITIONS = 1000 * 1000;

    private static final ObjectToSerialize ITEM =
            new ObjectToSerialize(123L, true, 777, 100,
                    new double[]{1.21, 123.21, 123.21, 521.21, 63.432, 213.436},
                    new long[]{5, 2, 6, 5, 1, 12});

    public static void main(String[] args) throws Exception {
//        performForObject();
//        performForObjects();
    }

    private static void performForObject() throws Exception {
        final TestCase[] testCases = getTestCases();

        System.out.println("----~~OBJECT SERIALIZATION~~----");
        for (final TestCase testCase : testCases) {
            for (int i = 0; i < 5; i++) {
                testCase.performTest();
                System.out.format("%d %s\twrite=%,dns read=%,dns total=%,dns\n",
                        i,
                        testCase.getName(),
                        testCase.getWriteTimeNanos(),
                        testCase.getReadTimeNanos(),
                        testCase.getWriteTimeNanos() +
                                testCase.getReadTimeNanos());

                if (!ITEM.equals(testCase.getOutput())) {
                    throw new IllegalStateException("Objects do not match");
                }

                System.gc();
                Thread.sleep(4000);
            }
        }
    }

    private static void performForObjects() throws Exception {
        final ObjectToSerialize[] objects = new ObjectToSerialize[100_000_0];

        for (int i = 0; i < objects.length; i++) {
            objects[i] = new ObjectToSerialize(123L, true, 777, 100,
                    new double[]{1.21, 123.21, 123.21, 521.21, 63.432, 213.436},
                    new long[]{5, 2, 6, 5, 1, 12});
        }
        final ArrayTestCase[] testCases = getTestCasesArray(objects);

        System.out.println("----~~ARRAY OF OBJECT SERIALIZATION~~----");
        for (final ArrayTestCase testCase : testCases) {
            for (int i = 0; i < 5; i++) {
                testCase.performTest();
                System.out.format("%d %s\twrite=%,dns read=%,dns total=%,dns\n",
                        i,
                        testCase.getName(),
                        testCase.getWriteTimeNanos(),
                        testCase.getReadTimeNanos(),
                        testCase.getWriteTimeNanos() +
                                testCase.getReadTimeNanos());

                if (!Arrays.equals(objects, testCase.getObjectsOutput())) {
                    throw new IllegalStateException("Objects do not match");
                }

                System.gc();
                Thread.sleep(4000);
            }
        }
    }

    private static TestCase[] getTestCases() {
        return new TestCase[]{
                new TestCase("Standard Java", REPETITIONS, ITEM) {

                    StandardJavaSerializer javaSerialization = new StandardJavaSerializer();

                    public void testWrite(final ObjectToSerialize item) throws Exception {
                        javaSerialization.write(item);
                    }

                    public ObjectToSerialize testRead() throws Exception {
                        return javaSerialization.read();
                    }
                },
                new TestCase("Direct ByteBuffer", REPETITIONS, ITEM) {

                    DirectByteBufferSerializer directByteBufferSerializer = new DirectByteBufferSerializer();

                    @Override
                    public void testWrite(ObjectToSerialize input) throws Exception {
                        directByteBufferSerializer.write(input);
                    }

                    @Override
                    public ObjectToSerialize testRead() throws Exception {
                        return directByteBufferSerializer.read();
                    }
                },
                new TestCase("ByteBuffer", REPETITIONS, ITEM) {

                    ByteBufferSerializer byteBufferSerializer = new ByteBufferSerializer();

                    @Override
                    public void testWrite(ObjectToSerialize input) throws Exception {
                        byteBufferSerializer.write(input);
                    }

                    @Override
                    public ObjectToSerialize testRead() throws Exception {
                        return byteBufferSerializer.read();
                    }
                },
                new TestCase("Unsafe Memory", REPETITIONS, ITEM) {

                    UnsafeMemorySerializer unsafeMemorySerializer  = new UnsafeMemorySerializer(new byte[1024]);

                    @Override
                    public void testWrite(ObjectToSerialize input) throws Exception {
                        unsafeMemorySerializer.write(input);
                    }

                    @Override
                    public ObjectToSerialize testRead() throws Exception {
                        return unsafeMemorySerializer.read();
                    }
                },
        };
    }

    private static ArrayTestCase[] getTestCasesArray(final ObjectToSerialize[] items) {
        return new ArrayTestCase[] {
                new ArrayTestCase("Standard Java", items) {

                    StandardJavaSerializer javaSerialization = new StandardJavaSerializer();

                    public void testWrite(final ObjectToSerialize[] items) throws Exception {
                        javaSerialization.writeArray(items);
                    }

                    public ObjectToSerialize[] testRead() throws Exception {
                        return javaSerialization.readArray(items);
                    }
                },
                new ArrayTestCase("Direct ByteBuffer", items) {

                    DirectByteBufferSerializer directByteBufferSerializer = new DirectByteBufferSerializer();

                    @Override
                    protected void testWrite(ObjectToSerialize[] objectsInput) throws Exception {
                        directByteBufferSerializer.writeArray(objectsInput);
                    }

                    @Override
                    public ObjectToSerialize[] testRead() throws Exception {
                        return directByteBufferSerializer.readArray(items);
                    }
                },
                new ArrayTestCase("ByteBuffer", items) {

                    ByteBufferSerializer byteBufferSerializer = new ByteBufferSerializer();

                    @Override
                    protected void testWrite(ObjectToSerialize[] objectsInput) throws Exception {
                        byteBufferSerializer.writeArray(objectsInput);
                    }

                    @Override
                    public ObjectToSerialize[] testRead() throws Exception {
                        return byteBufferSerializer.readArray(items);
                    }
                },
                new ArrayTestCase("Unsafe Memory", items) {

                    UnsafeMemorySerializer unsafeMemorySerializer = new UnsafeMemorySerializer(new byte[1024 * 1024 * 256]);

                    @Override
                    protected void testWrite(ObjectToSerialize[] objectsInput) throws Exception {
                        unsafeMemorySerializer.writeArray(objectsInput);
                    }

                    @Override
                    public ObjectToSerialize[] testRead() throws Exception {
                        return unsafeMemorySerializer.readArray(items);
                    }
                }
        };
    }
}
