package com.unsafe.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public final class UnsafeUtil {

    private UnsafeUtil() {}

    private static final Unsafe unsafe = makeUnsafe();

    public static Unsafe getUnsafe() {
        return unsafe;
    }

    public static Unsafe makeUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException("Unable to get unsafe!!!");
    }

    public static long sizeOf(Object object){
        return getUnsafe().getAddress(
                normalize(getUnsafe().getInt(object, 4L)) + 16L);
    }

    public  static long normalize(int value) {
        if(value >= 0) return value;
        return (~0L >>> 32) & value;
    }
}
