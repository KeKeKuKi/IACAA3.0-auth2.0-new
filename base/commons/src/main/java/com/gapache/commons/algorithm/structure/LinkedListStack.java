package com.gapache.commons.algorithm.structure;

import lombok.Data;

/**
 * 链表实现栈
 *
 * @author HuSen
 * @since 2020/7/20 10:22 上午
 */
public class LinkedListStack<E> {

    private StackNode<E> top;
    private final int maxSize;
    private int size;

    public LinkedListStack(int maxSize) {
        this.maxSize = maxSize;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public boolean isFull() {
        return size == maxSize;
    }

    public void push(E e) {
        if (isFull()) {
            throw new RuntimeException("is full!");
        }
        if (top == null) {
            top = new StackNode<>();
        } else {
            StackNode<E> temp = this.top;
            top = new StackNode<>();
            top.setNext(temp);
        }
        top.setData(e);
    }

    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("is empty!");
        }
        E data = top.getData();
        top = top.getNext();
        return data;
    }

    public static void main(String[] args) {
        LinkedListStack<Integer> stack = new LinkedListStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}

@Data
class StackNode<E> {
    private E data;
    private StackNode<E> next;
}
