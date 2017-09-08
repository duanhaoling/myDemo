package com.example.mydemo.rx.rxjava;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Created by ldh on 2016/11/22 0022.
 * 一般而言，只要类时自己管理内存，程序员就应该警惕内存泄漏问题
 */

public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null;//Eliminate obsolete reference
        return result;
    }

    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf(elements, 2 * size + 1);
    }
}
