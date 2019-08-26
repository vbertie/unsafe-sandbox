package com.unsafe.zeroGc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class OffHeapZeroGc {

    public static Unsafe unsafe;

    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception ex) {
            // known
        }
    }

    private static final int RECORDS = 50 * 1000 * 1000;
    private static long address;
    private static DirectMemoryTransaction flyweight = new DirectMemoryTransaction();
    private static LocalDateTime flyweightDate = LocalDateTime.now();
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            System.gc();
            performZeroGcTest(i);
        }
    }

    private static void performZeroGcTest(final int run) {
        long start = System.currentTimeMillis();

        init();

        System.out.printf("Memory %,d total, %,d free\n",
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().freeMemory());

        long sumOfEven = 0;
        long sumOfOdd = 0;

        for (int i = 0; i < RECORDS; i++) {

            final DirectMemoryTransaction t = get(i);

            if (t.getCode() == 'O') {
                sumOfOdd++;
            } else {
                sumOfEven++;
            }
        }

        long duration = System.currentTimeMillis() - start;
        System.out.printf("Run %d, duration %d ms\n", run, duration);
        System.out.println("even: " + sumOfEven + " odd: " + sumOfOdd);
        unsafe.freeMemory(address);
    }

    private static DirectMemoryTransaction get(final int i) {
        final long offset = address + (DirectMemoryTransaction.getObjectSize() * i);
        flyweight.setObjectOffset(offset);
        return flyweight;
    }

    private static void init() {

        final long reqHeap = DirectMemoryTransaction.getObjectSize() * RECORDS;
        address = unsafe.allocateMemory(reqHeap);

        for (int i = 0; i < RECORDS; i++) {
            DirectMemoryTransaction t = get(i);
            t.setTransId(i);
            t.setAmount(i * 2);
            t.setAccountNumber(777);
            t.setCode((i & 0b1) == 0 ? 'O' : 'E');
            t.setTransactionTime(1L);
        }
    }

    private static class DirectMemoryTransaction {

        private static long offset = 0;

        private static final long transIdOffset = offset += 0;
        private static final long accountNumberOffset = offset += 8;
        private static final long transactionTimeOffset = offset += 8;
        private static final long codeOffset = offset += 8;
        private static final long amountOffset = offset += 2;

        private static final long objectSize = offset += 8;

        private static long objectOffset;


        public void setObjectOffset(long offset) {
            objectOffset = offset;
        }

        public void setTransId(final long transId) {
            unsafe.putLong(objectOffset + transIdOffset, transId);
        }

        public void setAccountNumber(final long accountNumber) {
            unsafe.putLong(objectOffset + accountNumberOffset, accountNumber);
        }

        public void setTransactionTime(final long transactionTime) {
            unsafe.putLong(objectOffset + transactionTimeOffset, transactionTime);
        }

        public void setCode(final char code) {
            unsafe.putChar(objectOffset + codeOffset, code);
        }

        public void setAmount(final double amount) {
            unsafe.putDouble(objectOffset + amountOffset, amount);
        }

        public static long getOffset() {
            return offset;
        }

        public static long getTransId() {
            return unsafe.getLong(objectOffset + transactionTimeOffset);
        }

        public static long getAccountNumber() {
            return unsafe.getLong(objectOffset + accountNumberOffset);
        }

        public static long getTransactionTime() {
            return unsafe.getLong(objectOffset + transactionTimeOffset);
        }

        public static char getCode() {
            return unsafe.getChar(objectOffset + codeOffset);
        }

        public static double getAmount() {
            return unsafe.getDouble(objectOffset + amountOffset);
        }

        public static long getObjectSize() {
            return objectSize;
        }
    }
}
