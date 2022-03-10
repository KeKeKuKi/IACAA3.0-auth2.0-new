package com.gapache.commons.algorithm.structure;

/**
 * 双向链表，无顺序版，默认添加到链表的最后
 *
 * @author HuSen
 * create on 2020/6/3 00:53
 */
public class DoubleLinkedList {

    private final Node head = new Node(0, null, null);

    private Node tail;

    public void add(Node node) {
        if (tail == null) {
            tail = node;
            head.setNext(node);
            return;
        }
        tail.setNext(node);
        node.setPre(tail);
        tail = node;
    }

    public void delete(Node node) {
        Node next = head.getNext();
        while (next != null) {
            if (next.getNo() == node.getNo()) {
                // 删除的是第一个元素
                if (next.getPre() == null) {
                    head.setNext(next.getNext());
                }
                // 删除的是最后一个元素
                if (next.getNext() == null) {
                    tail = next.getPre();
                }
                // 中间的元素
                if (next.getPre() != null && next.getNext() != null) {
                    next.getPre().setNext(next.getNext());
                    next.getNext().setPre(next.getPre());
                }
            }
            next = next.getNext();
        }
    }

    public void update(Node node) {
        Node pre = tail;
        do {
            if (pre.getNo() == node.getNo()) {
                pre.setName(node.getName());
                pre.setNickName(node.getNickName());
                break;
            }
            pre = pre.getPre();
        } while (pre != null);
    }

    public void list() {
        Node temp = head.getNext();
        while (temp != null) {
            System.out.println(temp);
            temp = temp.getNext();
        }
        System.out.println("========================");
    }

    public static void main(String[] args) {
        DoubleLinkedList doubleLinkedList = new DoubleLinkedList();
        doubleLinkedList.add(new Node(1, "1", "1"));
        doubleLinkedList.add(new Node(3, "3", "3"));
        doubleLinkedList.add(new Node(5, "5", "5"));
        doubleLinkedList.list();

        doubleLinkedList.update(new Node(5, "5", "five"));
        doubleLinkedList.list();

        doubleLinkedList.delete(new Node(3, "3", "3"));
        doubleLinkedList.list();
    }
}
