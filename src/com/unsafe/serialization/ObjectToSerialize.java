package com.unsafe.serialization;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class ObjectToSerialize implements Serializable {

        private static final long serialVersionUID = 10275539472837495L;

        private final long sourceId;
        private final boolean special;
        private final int orderCode;
        private final int priority;
        private final double[] prices;
        private final long[] quantities;

        public ObjectToSerialize(final long sourceId, final boolean special,
                                    final int orderCode, final int priority,
                                    final double[] prices, final long[] quantities) {
            this.sourceId = sourceId;
            this.special = special;
            this.orderCode = orderCode;
            this.priority = priority;
            this.prices = prices;
            this.quantities = quantities;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectToSerialize that = (ObjectToSerialize) o;
        return sourceId == that.sourceId &&
                special == that.special &&
                orderCode == that.orderCode &&
                priority == that.priority &&
                Arrays.equals(prices, that.prices) &&
                Arrays.equals(quantities, that.quantities);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sourceId, special, orderCode, priority);
        result = 31 * result + Arrays.hashCode(prices);
        result = 31 * result + Arrays.hashCode(quantities);
        return result;
    }

    public long getSourceId() {
        return sourceId;
    }

    public boolean isSpecial() {
        return special;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public int getPriority() {
        return priority;
    }

    public double[] getPrices() {
        return prices;
    }

    public long[] getQuantities() {
        return quantities;
    }
}
