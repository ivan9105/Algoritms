package com.certificate.collections;

import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.LinkedList;

public class CollectionsMockTest {
    @Test
    public void collectionsSortTest() {
        LinkedList<String> list = new LinkedList<>();
        list.add("BbB1");
        list.add("bBb2");
        list.add("bbB3");
        list.add("BBB3");
        list.add("BBb3");
        list.add("BBb4");
        Collections.sort(list);
        //first priority upper case B < b
        //second digits
        Assert.assertEquals(list.toString(), "[BBB3, BBb3, BBb4, BbB1, bBb2, bbB3]");
    }

    //Question 1
//        List list = new LinkedList<String>();
//        list.add("one");
//        list.add("two");
//        list.add("three");
//
//        Collections.reverse(list);
//        Iterator iter = list.iterator();
//
//        for (Object o : iter) {
//            System.out.print(o + " ");
//        }
//Compilation fails ListIterator can not implements java.lang.Iterable
}
