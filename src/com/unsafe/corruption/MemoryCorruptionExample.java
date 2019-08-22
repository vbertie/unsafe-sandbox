package com.unsafe.corruption;

import com.unsafe.util.UnsafeUtil;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class MemoryCorruptionExample {

    private static final Unsafe u = UnsafeUtil.getUnsafe();

    public static void main(String[] args) throws NoSuchFieldException {
        Security security = new Security();
        Security security2 = new Security();
        System.out.println(security.isAccessGiven()); // false

        Field f = Security.class.getDeclaredField("SECURITY_ACCESS");
        u.putInt(security, u.objectFieldOffset(f), 666);

        System.out.println(security.isAccessGiven()); // true
    }
}

class Security {
    private int SECURITY_ACCESS = 1;

    public boolean isAccessGiven() {
        return 666 == SECURITY_ACCESS;
    }
}
