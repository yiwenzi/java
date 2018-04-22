package com.joker.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter on 2018/2/6.
 */
public class StringArray {
    public static void main(String[] args) {
        String hql = "123";
        String[] array = {hql, "0"};
        printArray(array);
        hql = "456";
        printArray(array);

        List<String> list = new ArrayList<>();
        list.add(hql);
        list.add("0");
        printList(list);
        hql = "789";
        printList(list);


        Person[] persons = new Person[2];
        List<Person> list2 = new ArrayList<>();
        Person person = new Person();
        Person person2 = new Person();
        list2.add(person);
        list2.add(person2);

        persons[0] = person;
        persons[1] = person2;
        //printList(list2);
        printArray(persons);
        person.setName("joker");
        person.setAge(11);
        //printList(list2);
        printArray(persons);
    }
    public static void printArray(String[] array) {
        if(array == null) {
            System.out.println("null");
        } else {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                System.out.println(array[i]);
            }
        }
    }

    public static void printArray(Person[] array) {
        if(array == null) {
            System.out.println("null");
        } else {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                System.out.println(array[i]);
            }
        }
    }

    public static void printList(List list) {
        if(list == null) {
            System.out.println("null");
        } else {
            int length = list.size();
            for (int i = 0; i < length; i++) {
                System.out.println(list.get(i));
            }
        }
    }
}
