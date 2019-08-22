package com.unsafe.util;

import com.unsafe.serialization.ObjectToSerialize;

import java.nio.ByteBuffer;

public final class ByteBufferUtil {

    private ByteBufferUtil() {}

    public static void serialize(ObjectToSerialize item, ByteBuffer byteBuffer) {
        byteBuffer.putLong(item.getSourceId());
        byteBuffer.put((byte)(item.isSpecial() ? 1 : 0));
        byteBuffer.putInt(item.getOrderCode());
        byteBuffer.putInt(item.getPriority());

        byteBuffer.putInt(item.getPrices().length);
        byteBuffer.asDoubleBuffer().put(item.getPrices());
        byteBuffer.position(byteBuffer.position() + item.getPrices().length * 8);

        byteBuffer.putInt(item.getQuantities().length);
        for (final long quantity : item.getQuantities())
        {
            byteBuffer.putLong(quantity);
        }
    }

    public static ObjectToSerialize deserialize(ByteBuffer byteBuffer) {
        final long sourceId = byteBuffer.getLong();
        final boolean special = 0 != byteBuffer.get();
        final int orderCode = byteBuffer.getInt();
        final int priority = byteBuffer.getInt();

        final int pricesSize = byteBuffer.getInt();
        final double[] prices = new double[pricesSize];
        for (int i = 0; i < pricesSize; i++)
        {
            prices[i] = byteBuffer.getDouble();
        }

        final int quantitiesSize = byteBuffer.getInt();
        final long[] quantities = new long[quantitiesSize];
        for (int i = 0; i < quantitiesSize; i++)
        {
            quantities[i] = byteBuffer.getLong();
        }

        return new ObjectToSerialize(sourceId, special, orderCode,
                priority, prices, quantities);
    }
}
