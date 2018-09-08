package com.certificate.api;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ApiMockTest {
    @Test
    public void unreachableNullPointerException() {
        try {
            String arr[] = new String[10];
            arr = null;
            arr[0] = "one";
            System.out.print(arr[0]);
            throw new NullPointerException();
        } catch (Exception ex) {
            System.out.print("exception");
        }
        //Compilation ERROR cause NullPointerException is Runtime not checked exception;
//        catch(NullPointerException nex) {
//            System.out.print("null pointer exception");
//        }
    }

    @Test
    public void cannotCompareEnumsWithGtOrLt() {
        assertTrue(State.ACTIVE == State.ACTIVE);
        assertTrue(State.ACTIVE.equals(State.ACTIVE));
        assertFalse(State.ACTIVE == State.INACTIVE);
        //        assertTrue(State.ACTIVE < State.INACTIVE);
    }

    enum State {ACTIVE, INACTIVE, DELETED}

    //Question 1
//    private static class StaticHotel {
//        //A) Method book() can directly call method cancelBooking() NO, non static method, can not be references from static context
//        //B) Method cancelBooking() can directly call method book() YES
//        //C) Hotel.book() is a valid invocation of book() YES
//        //D) Hotel.cancelBooking() is a valid invocation of cancelBooking() NO, it's no static method
//        public static void book() {
//
//        }
//
//        public void cancelBooking() {
//            book();
//        }
//    }
}
