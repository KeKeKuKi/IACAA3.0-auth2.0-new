package com.gapache.commons.algorithm.structure;

import lombok.Data;

/**
 * 约瑟夫问题
 *
 * @author HuSen
 * @since 2020/7/18 8:32 下午
 */
public class JosephQuestion {
    private People first;
    private People last;

    private void init(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("人数不能小于1!");
        }
        if (n == 1) {
            return;
        }
        for (int i = 0; i < n; i++) {
            People newPeople = new People(i);
            if (first == null) {
                first = newPeople;
                last = first;
                continue;
            }
            last.setNext(newPeople);
            newPeople.setPre(last);
            newPeople.setNext(first);
            first.setPre(newPeople);
            last = newPeople;
        }
    }

    private int run(int n, int m) {
        if (n == 1) {
            return n;
        }
        // 初始化双向环形链表
        init(n);
        int i = 1;
        People current = first;
        // 当下一个不是自己的时候，说明自己已经时最后一个了
        while (current.getNext() != current) {
            if (i % m == 0) {
                current.getPre().setNext(current.getNext());
                current.getNext().setPre(current.getPre());
                i = 0;
            }
            current = current.getNext();
            i++;
        }
        return current.getNo() + 1;
    }

    public static void main(String[] args) {
        JosephQuestion question = new JosephQuestion();
        System.out.println(question.run(5, 3));
    }
}

@Data
class People {
    private int no;
    private People next;
    private People pre;

    public People(int no) {
        this.no = no;
    }
}
