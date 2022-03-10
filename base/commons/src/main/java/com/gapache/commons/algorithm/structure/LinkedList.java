package com.gapache.commons.algorithm.structure;

import lombok.Getter;
import lombok.Setter;

import java.util.Stack;

/**
 * 单链表，有顺序的
 *
 * @author HuSen
 * @since 2020/5/29 5:13 下午
 */
public class LinkedList {
    /** 标识节点，下一个节点是链表头 */
    private Node sign = new Node(0, null, null);

    public void addByNo(Node node) {
        // 空，直接设置为头节点
        if (isEmpty()) {
            sign.setNext(node);
            return;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        // 比头结点还要小，直接设置为头节点
        if (node.getNo() < temp.getNo()) {
            node.setNext(temp);
            sign.setNext(node);
            return;
        }
        while (temp != null) {
            Node tempNext = temp.getNext();
            // 没有尾节点了，说明找到最后了
            if (tempNext == null) {
                temp.setNext(node);
                break;
            }
            // 找到了
            if (node.getNo() > temp.getNo() && node.getNo() < tempNext.getNo()) {
                // 找到了
                temp.setNext(node);
                node.setNext(tempNext);
                break;
            }
            // 找到了相等的说明已存在，不进行任何操作
            if (node.getNo() == temp.getNo()) {
                break;
            }
            temp = temp.getNext();
        }
    }

    public void update(Node node) {
        if (isEmpty()) {
            System.out.println("节点为空!!!");
            return;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        while (temp != null) {
            if (temp.getNo() == node.getNo()) {
                temp.setName(node.getName());
                temp.setNickName(node.getNickName());
                break;
            }
            temp = temp.getNext();
        }
    }

    public Node remove(int no) {
        if (isEmpty()) {
            System.out.println("节点为空!!!");
            return null;
        }
        // 获取到头节点
        Node temp = sign.getNext();
        // 是头节点
        if (temp.getNo() == no) {
            sign.setNext(temp.getNext());
            return temp;
        }
        while (temp != null) {
            Node next = temp.getNext();
            if (next == null) {
                return null;
            }
            if (next.getNo() == no) {
                temp.setNext(next.getNext());
                next.setNext(null);
                return next;
            }
            temp = temp.getNext();
        }
        return null;
    }

    public void list() {
        Node temp = sign.getNext();
        while (temp != null) {
            System.out.println(temp);
            temp = temp.getNext();
        }
    }

    public boolean isEmpty() {
        return sign.getNext() == null;
    }

    public int size() {
        int size = 0;
        Node next = sign.getNext();
        while (next != null) {
            size++;
            next = next.getNext();
        }
        return size;
    }

    /**
     * 找到链表的倒数第index个元素
     *
     * @param index 倒数第几个
     * @return 找到的元素
     */
    public Node findByReciprocal(int index) {
        int size = size();
        if (index > size) {
            System.out.println("没有找到!!!");
            return null;
        }
        int target = size - index;
        Node result = sign.getNext();
        for (int i = 0; i < target; i++) {
            result = result.getNext();
        }
        return result;
    }

    /**
     * 单链表的反转
     */
    public void reverse() {
        if (isEmpty()) {
            System.out.println("链表为空!!!");
        }
        Node newLinkedList = new Node(0, null, null);
        Node next = sign.getNext();
        while (next != null) {
            // 保存临时节点
            Node temp = next;
            next = next.getNext();
            temp.setNext(newLinkedList.getNext());
            // 新找到的元素作为头节点
            newLinkedList.setNext(temp);
        }
        sign = newLinkedList;
    }

    /**
     * 从尾到头打印链表
     */
    public void reverseList() {
        Stack<Node> stack = new Stack<>();
        Node next = sign.getNext();
        while (next != null) {
            stack.push(next);
            next = next.getNext();
        }
        while (!stack.empty()) {
            System.out.println(stack.pop());
        }
    }

    /**
     * 合并两个有序的单链表，合并之后的单链表依然有序
     *
     * @param merged 被合并的链表
     * @return 合并后的新链表
     */
    public LinkedList merge(LinkedList merged) {
        LinkedList newLinkedList = new LinkedList();
        if (merged.isEmpty() || this.isEmpty()) {
            return newLinkedList;
        }
        Node head = null;
        Node temp = null;
        Node selfHead = sign.getNext();
        Node mergedHead = merged.sign.getNext();
        while (selfHead != null || mergedHead != null) {
            if (selfHead == null) {
                if (temp != null) {
                    temp.setNext(mergedHead);
                }
                temp = mergedHead;
                mergedHead = mergedHead.getNext();
                continue;
            }
            if (mergedHead == null) {
                if (temp != null) {
                    temp.setNext(selfHead);
                }
                temp = selfHead;
                selfHead = selfHead.getNext();
                continue;
            }
            if (selfHead.getNo() <= mergedHead.getNo()) {
                if (head == null) {
                    head = selfHead;
                }
                if (temp != null) {
                    temp.setNext(selfHead);
                }
                temp = selfHead;
                selfHead = selfHead.getNext();
                continue;
            }
            if (selfHead.getNo() > mergedHead.getNo()) {
                if (temp != null) {
                    temp.setNext(mergedHead);
                }
                temp = mergedHead;
                mergedHead = mergedHead.getNext();
            }
            if (head == null) {
                head = temp;
            }
        }
        newLinkedList.sign.setNext(head);
        return newLinkedList;
    }

    public static void main(String[] args) {
        LinkedList linkedList = new LinkedList();
        linkedList.addByNo(new Node(2, "2", "2"));
        linkedList.list();
        linkedList.addByNo(new Node(1, "1", "1"));
        linkedList.list();
        linkedList.addByNo(new Node(4, "4", "4"));
        linkedList.list();
        linkedList.addByNo(new Node(3, "3", "4"));
        linkedList.list();

        linkedList.update(new Node(2, "2", "two"));
        linkedList.list();

        System.out.println("======================");
        System.out.println(linkedList.remove(4));
        System.out.println("======================");
        linkedList.list();
        System.out.println(linkedList.size());
        System.out.println(linkedList.findByReciprocal(3));
        System.out.println("======================");
        linkedList.reverse();
        linkedList.list();
        System.out.println("======================");
        linkedList.reverseList();
        System.out.println("======================");
        LinkedList linkedList2 = new LinkedList();
        linkedList2.addByNo(new Node(4, "4", "4"));
        linkedList2.addByNo(new Node(5, "5", "5"));
        linkedList2.addByNo(new Node(6, "6", "6"));
        linkedList2.addByNo(new Node(7, "7", "7"));
        linkedList.reverse();
        linkedList.list();
        System.out.println("======================");
        LinkedList merge = linkedList.merge(linkedList2);
        merge.list();
    }
}

@Getter
@Setter
class Node {
    private int no;
    private String name;
    private String nickName;
    private Node next;
    /** 双向链表用 */
    private Node pre;

    public Node(int no, String name, String nickName) {
        this.no = no;
        this.name = name;
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "Node{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
























