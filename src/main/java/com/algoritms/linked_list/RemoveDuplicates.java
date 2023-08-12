package com.algoritms.linked_list;

import java.util.Iterator;
import java.util.LinkedList;

public class RemoveDuplicates {
    public static void main(String[] args) {
        var list = new LinkedList<Integer>();
        list.add(4);
        list.add(5);
        list.add(5);
        list.add(4);

        removeDuplicates(list.iterator());

        //TODO нужен custom linked list
        //TODO два решение кеш через set либо в цикле получаем элемент и во внутреннем цикле удаляем все дубли
    }

    private static void removeDuplicates(Iterator<Integer> iterator) {
        //TODO
    }
}
