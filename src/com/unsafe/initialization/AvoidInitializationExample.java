package com.unsafe.initialization;


import com.unsafe.util.UnsafeUtil;
import sun.misc.Unsafe;


public class AvoidInitializationExample {

    private static final Unsafe u = UnsafeUtil.getUnsafe();

    public static void main(String[] args) throws InstantiationException {
        Employee employee = (Employee) u.allocateInstance(Employee.class); // its allocated off-heap
        employee.yell();
    }
}

/* not instantiable */
final class Employee {

    private long id;

    private Employee() { }

    public void yell() {
        System.out.println("Yelling!");
    }
}
