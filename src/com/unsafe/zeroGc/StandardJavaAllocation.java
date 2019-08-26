package com.unsafe.zeroGc;

import java.util.Date;

public class StandardJavaAllocation {

    private static final int RECORDS = 50 * 1000 * 1000;
    private static Transaction[] transactions;

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.gc();
            performStandardJavaTest(i);
        }
    }

    private static void performStandardJavaTest(final int run) {
        long start = System.currentTimeMillis();

        init();

        System.out.printf("Memory %,d total, %,d free\n",
                Runtime.getRuntime().totalMemory(),
                Runtime.getRuntime().freeMemory());

        long sumOfEven = 0;
        long sumOfOdd = 0;

        for (int i = 0; i < RECORDS; i++) {

            Transaction t = transactions[i];

            if (t.getCode() == 'O') {
                sumOfOdd++;
            } else {
                sumOfEven++;
            }
        }

        long duration = System.currentTimeMillis() - start;
        System.out.printf("Run %d, duration %d ms\n", run, duration);
    }

    private static void init() {

        transactions = new Transaction[RECORDS];

        for (int i = 0; i < RECORDS; i++) {
            Transaction t = new Transaction();
            transactions[i] = t;
            t.setTransId(i);
            t.setAmount(i * 2);
            t.setAccountNumber(777);
            t.setCode((i & 0b1) == 0 ? 'O' : 'E');
            t.setTransactionTime(new Date().getTime());
        }

    }
    private static class Transaction {

        private long transId;
        private long accountNumber;
        private long transactionTime;
        private char code;
        private double amount;

        public void setTransId(long transId) {
            this.transId = transId;
        }

        public void setAccountNumber(long accountNumber) {
            this.accountNumber = accountNumber;
        }

        public void setTransactionTime(long transactionTime) {
            this.transactionTime = transactionTime;
        }

        public void setCode(char code) {
            this.code = code;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public long getTransId() {
            return transId;
        }

        public long getAccountNumber() {
            return accountNumber;
        }

        public long getTransactionTime() {
            return transactionTime;
        }

        public char getCode() {
            return code;
        }

        public double getAmount() {
            return amount;
        }
    }

}
