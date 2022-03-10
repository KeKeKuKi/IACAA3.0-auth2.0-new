package com.gapache.commons.algorithm.structure;

/**
 * 基于数组实现栈
 *
 * @author HuSen
 * @since 2020/7/19 7:25 下午
 */
public class ArrayStack {
    private int top;
    private final int maxSize;
    private final int[] stack;

    public ArrayStack(int maxSize) {
        this.maxSize = maxSize;
        this.top = -1;
        this.stack = new int[maxSize];
    }

    public void push(int e) {
        if (isFull()) {
            throw new RuntimeException("is full!");
        }
        this.top++;
        stack[this.top] = e;
    }

    public int pop() {
        if (isEmpty()) {
            throw new RuntimeException("is empty!");
        }
        int popE = stack[this.top];
        stack[this.top] = 0;
        this.top--;
        return popE;
    }

    public boolean isEmpty() {
        return this.top == -1;
    }

    public boolean isFull() {
        return this.top + 1 == maxSize;
    }

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
