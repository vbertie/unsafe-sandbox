package com.unsafe.serialization;

import com.unsafe.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.util.Objects;

import static com.unsafe.serialization.SerializationExample.REPETITIONS;

public class UnsafeMemorySerializer {

    private static final Unsafe u = UnsafeUtil.getUnsafe();

    private static final long byteArrayOffset = u.arrayBaseOffset(byte[].class);
    private static final long longArrayOffset = u.arrayBaseOffset(byte[].class);
    private static final long doubleArrayOffset = u.arrayBaseOffset(byte[].class);

    private static final int SIZE_OF_BOOLEAN = 1;
    private static final int SIZE_OF_INTEGER = 4;
    private static final int SIZE_OF_LONG = 8;

    private static int pos = 0;
    private final byte[] buffer;

    public UnsafeMemorySerializer(byte[] buffer) {
        Objects.requireNonNull(buffer);
        this.buffer = buffer;
    }

    public void write(ObjectToSerialize item) {
        for (int i = 0; i < REPETITIONS; i++) {
            reset();
            putLong(item.getSourceId());
            putBoolean(item.isSpecial());
            putInt(item.getOrderCode());
            putInt(item.getPriority());
            putDoubleArray(item.getPrices());
            putLongArray(item.getQuantities());
        }
    }

    public ObjectToSerialize read() {
        ObjectToSerialize object = null;
        for (int i = 0; i < REPETITIONS; i++)
        {
            reset();
            final long sourceId = getLong();
            final boolean special = getBoolean();
            final int orderCode = getInt();
            final int priority = getInt();
            final double[] prices = getDoubleArray();
            final long[] quantities = getLongArray();

            object = new ObjectToSerialize(sourceId, special, orderCode,
                    priority, prices, quantities);
        }
        return object;
    }

    public void writeArray(ObjectToSerialize[] array) {
        reset();
        for (ObjectToSerialize item : array) {
            putLong(item.getSourceId());
            putBoolean(item.isSpecial());
            putInt(item.getOrderCode());
            putInt(item.getPriority());
            putDoubleArray(item.getPrices());
            putLongArray(item.getQuantities());
        }
    }

    public ObjectToSerialize[] readArray(ObjectToSerialize[] array) {
        reset();
        ObjectToSerialize[] deserialized = new ObjectToSerialize[array.length];
        for (int i = 0; i < array.length; i++) {
            final long sourceId = getLong();
            final boolean special = getBoolean();
            final int orderCode = getInt();
            final int priority = getInt();
            final double[] prices = getDoubleArray();
            final long[] quantities = getLongArray();

            ObjectToSerialize objectToSerialize = new ObjectToSerialize(sourceId, special, orderCode,
                    priority, prices, quantities);
            deserialized[i] = objectToSerialize;
        }
        return deserialized;
    }

    private void putLong(long val) {
        u.putLong(buffer, byteArrayOffset + pos, val);
        pos += SIZE_OF_LONG;
    }

    private long getLong() {
        long val = u.getLong(buffer , byteArrayOffset + pos);
        pos += SIZE_OF_LONG;
        return val;
    }

    private void putInt(int val) {
        u.putInt(buffer, byteArrayOffset + pos, val);
        pos += SIZE_OF_INTEGER;
    }

    private int getInt() {
        int val = u.getInt(buffer , byteArrayOffset + pos);
        pos += SIZE_OF_INTEGER;
        return val;
    }

    private void putBoolean(final boolean value) {
        u.putBoolean(buffer, byteArrayOffset + pos, value);
        pos += SIZE_OF_BOOLEAN;
    }

    private boolean getBoolean() {
        boolean value = u.getBoolean(buffer, byteArrayOffset + pos);
        pos += SIZE_OF_BOOLEAN;
        return value;
    }

    private void putLongArray(long[] array) {
        putInt(array.length);
        long bytesToCopy = array.length << 3;
        u.copyMemory(array, longArrayOffset, buffer,
                byteArrayOffset + pos, bytesToCopy);
        pos += bytesToCopy;
    }

    private long[] getLongArray() {
        int arraySize = getInt();
        long[] values = new long[arraySize];
        long bytesToCopy = values.length << 3;
        u.copyMemory(buffer, byteArrayOffset + pos,
                values, longArrayOffset, bytesToCopy);
        pos += bytesToCopy;
        return values;
    }

    private void putDoubleArray(final double[] values)
    {
        putInt(values.length);

        long bytesToCopy = values.length << 3;
        u.copyMemory(values, doubleArrayOffset,
                buffer, byteArrayOffset + pos,
                bytesToCopy);
        pos += bytesToCopy;
    }

    private double[] getDoubleArray() {
        int arraySize = getInt();
        double[] values = new double[arraySize];

        long bytesToCopy = values.length << 3;
        u.copyMemory(buffer, byteArrayOffset + pos,
                values, doubleArrayOffset,
                bytesToCopy);
        pos += bytesToCopy;

        return values;
    }

    private void reset() {
        pos = 0;
    }
}
