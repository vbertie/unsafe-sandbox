package com.unsafe.cas;

import com.unsafe.util.UnsafeUtil;
import sun.misc.Unsafe;

public class CASOperationExample {
    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {
        Counter c = new Counter();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c.increment();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                c.increment();
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println(c.getValue());
    }
}

class Counter {

    private static Unsafe u = UnsafeUtil.getUnsafe();
    private final long offset;
    private int value;

    public Counter() throws NoSuchFieldException {
        offset = u.objectFieldOffset(Counter.class.getDeclaredField("value"));
    }

    public int getValue() {
        return value;
    }

    public void increment() {
        int before = value;
        while (!u.compareAndSwapInt(this, offset, before, before +1)) {
            before = value;
        }
    }

}
