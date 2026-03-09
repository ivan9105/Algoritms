package com.certificate.inheritance;

import org.junit.Assert;
import org.junit.Test;

public class InheritanceMockTest {
    @Test
    public void useSuperclassCallMethodTest() {
        SuperHotel hotel = new SuperHotel();
        hotel.book(2);
        //first call bookings-- child class = -1
        //second call bookings++ parent class = 0
        //third call bookings += size = 2
        Assert.assertEquals(hotel.bookings, 2);
    }

    @Test
    public void compilationError() {
        Hotel hotel = new SuperHotel();
        //Cause parent class can not has method book with param(int size)
        //hotel.book(2);
    }

    private class SuperHotel extends Hotel {
        public void book() {
            bookings--;
        }

        public void book(int size) {
            book();
            super.book();
            bookings += size;
        }
    }

    private class Hotel {
        public int bookings;
        public void book() {
            bookings++;
        }
    }
}
