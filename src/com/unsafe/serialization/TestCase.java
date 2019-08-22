package com.unsafe.serialization;

public abstract class TestCase {
    private final String name;
    private final int repetitions;
    private final ObjectToSerialize input;
    private ObjectToSerialize output;
    private long writeTimeNanos;
    private long readTimeNanos;;

    public TestCase(String name, int repetitions, ObjectToSerialize input) {
        this.name = name;
        this.repetitions = repetitions;
        this.input = input;
    }

    public void performTest() throws Exception {
        final long startWriteNanos = System.nanoTime();
        testWrite(input);
        writeTimeNanos = (System.nanoTime() - startWriteNanos) / repetitions;

        final long startReadNanos = System.nanoTime();
        output = testRead();
        readTimeNanos = (System.nanoTime() - startReadNanos) / repetitions;
    }

    public int getRepetitions() {
        return repetitions;
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

    public ObjectToSerialize getInput() {
        return input;
    }

    public ObjectToSerialize getOutput() {
        return output;
    }

    public abstract ObjectToSerialize testRead() throws Exception;
    public abstract void testWrite(ObjectToSerialize input) throws Exception;
}
