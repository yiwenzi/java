package com.joker.interview.linkedlist;

import com.joker.interview.common.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hunter on 2018/3/28.
 */
public class LinkedListCreator {
    /**
     * create a linked list
     * @param data
     * @return head of the linked list.The returned linked list ends with last node with getNext() == null
     */
    public Node createLinkedList(List<Integer> data) {
        if(data.isEmpty()) {
            return null;
        }
        Node fistNode = new Node(data.get(0));
        Node headOfSubList = createLinkedList(data.subList(1, data.size()));
        fistNode.setNext(headOfSubList);
        return fistNode;
    }
    public static void main(String[] args) {
        LinkedListCreator creator = new LinkedListCreator();
        Node.printLinkedList(creator.createLinkedList(new ArrayList<>()));
        Node.printLinkedList(creator.createLinkedList(Arrays.asList(1)));
        Node.printLinkedList(creator.createLinkedList(Arrays.asList(1, 2, 3, 4, 5)));
    }
}
