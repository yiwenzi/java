package com.joker.interview.linkedlist;

import com.joker.interview.common.Node;

/**
 * Created by hunter on 2018/3/29.
 */
public class LinkedListDeletor {

    public Node deleteIfEquals(Node head, int value) {
        while (head != null && head.getValue() == value) {
            head = head.getNext();
        }

        if(head == null) {
            return head;
        }

        Node pre = head;
        while (pre.getNext() != null) {
            if(pre.getNext().getValue() == value) {
                pre.setNext(pre.getNext().getNext());
            } else {
                pre = pre.getNext();
            }
        }
        return head;
    }

    public static void main(String[] args) {

    }
}
