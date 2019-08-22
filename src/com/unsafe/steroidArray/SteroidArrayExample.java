package com.unsafe.steroidArray;

import com.unsafe.util.UnsafeUtil;
import sun.misc.Unsafe;

public class SteroidArrayExample {

    public static void main(String[] args) {
        SteroidArray array = new SteroidArray(Integer.MAX_VALUE * 10L); //ultra-size
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            array.set(i, 50);
        }
        for (int i = 0; i < 100; i++) {
            sum += array.get(i);
        }
        System.out.println(sum);
    }
}

class SteroidArray {

    private static final Unsafe u = UnsafeUtil.getUnsafe();
    private final static int INTEGER = 4;
    private long size;
    private long address;

    public SteroidArray(long size) {
        this.size = size;
        address = u.allocateMemory(size);
    }

    public void set(long i, int value) {
        u.putInt(address + i * INTEGER, value);
    }

    public int get(long idx) {
        return u.getInt(address + idx * INTEGER);
    }

    public void freeMemory() {
        u.freeMemory(address);
    }
}