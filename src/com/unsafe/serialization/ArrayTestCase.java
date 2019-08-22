package com.unsafe.serialization;

public abstract class ArrayTestCase {
    private final String name;
    private ObjectToSerialize[] objectsInput;
    private ObjectToSerialize[] objectsOutput;
    private long writeTimeNanos;
    private long readTimeNanos;;

    public ArrayTestCase(String name, ObjectToSerialize[] objectsInput) {
        this.name = name;
        this.objectsInput = objectsInput;
    }

    public void performTest() throws Exception {
        final long startWriteNanos = System.nanoTime();
        testWrite(objectsInput);
        writeTimeNanos = (System.nanoTime() - startWriteNanos);

        final long startReadNanos = System.nanoTime();
        objectsOutput = testRead();
        readTimeNanos = (System.nanoTime() - startReadNanos);
    }

    public String getName() {
        return name;
    }

    public long getWriteTimeNanos() {
        return writeTimeNanos;
    }

    public long getReadTimeNanos() {
        return readTimeNanos;
    }

    public ObjectToSerialize[] getObjectsInput() {
        return objectsInput;
    }

    public ObjectToSerialize[] getObjectsOutput() {
        return objectsOutput;
    }

    protected abstract void testWrite(ObjectToSerialize[] objectsInput) throws Exception;
    public abstract ObjectToSerialize[] testRead() throws Exception;
}
