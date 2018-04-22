package com.joker.interview.linkedlist;

import com.joker.interview.common.Node;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hunter on 2018/3/28.
 */
public class LinkedListReverser {

    /**
     *
     * @param head
     * @return
     */
    public Node reverseLinkedList(Node head) {
        if(head == null || head.getNext() == null) {
            return head;
        }
        Node newHead = reverseLinkedList(head.getNext());
        head.getNext().setNext(head);
        head.setNext(null);
        return newHead;
    }

    /**
     * 编写注释 用英文
     * 思考的时候要用实际变量去推算
     * @param head
     * @return
     */
    public Node reverseLinkedList2(Node head) {
        //不要最开始就写特例，要用自己的程序也包含特例。
        if(head == null) {
            return head;
        }
        Node newHead = null;
        Node curHead = head;
        while (curHead != null) {
            Node next = curHead.getNext();
            curHead.setNext(newHead);
            newHead = curHead;
            curHead = next;
        }
        return newHead;
    }
    public static void main(String[] args) {
        LinkedListCreator creator = new LinkedListCreator();
        LinkedListReverser reverser = new LinkedListReverser();
        Node.printLinkedList(
                reverser.reverseLinkedList(creator.createLinkedList(new ArrayList<>())));
        Node.printLinkedList(
                reverser.reverseLinkedList(creator.createLinkedList(Arrays.asList(1))));
        Node.printLinkedList(
                reverser.reverseLinkedList(creator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5))));
    }
}
