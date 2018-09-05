package com.certificate.hash;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;

public class HashMockTest {
    @Test
    public void stringHashTest() {
        StringAsAttrClass h1 = new StringAsAttrClass("1");
        StringAsAttrClass h2 = new StringAsAttrClass("1");
        String s1 = new String("2");
        String s2 = new String("2");

        HashSet<Object> hs = new HashSet<Object>();
        hs.add(h1);
        hs.add(h2);
        hs.add(s1);
        hs.add(s2);

        //cause s1.hashcode == s2.hashcode, see java.lang.String hashcode() calc by char[] value, return s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
        Assert.assertEquals(hs.size(), 3);
    }



    private class StringAsAttrClass {
        private String str;

        public StringAsAttrClass(String str) {
            this.str = str;
        }
    }
}
