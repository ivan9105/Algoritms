package com.certificate.hash;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @Test
    public void objectHashTest() {
        StringAsAttrClassWithHashCodeAndEquals h1 = new StringAsAttrClassWithHashCodeAndEquals("1");
        StringAsAttrClassWithHashCodeAndEquals h2 = new StringAsAttrClassWithHashCodeAndEquals("1");
        String s1 = new String("2");
        String s2 = new String("2");

        HashSet<Object> hs = new HashSet<>();
        hs.add(h1);
        //hashMap consist "1" -> h1
        //if key equals by hashcode, then check equals method
        hs.add(h2);
        //hashMap consist "1" -> h1, h2 was skipped
        hs.add(s1);
        hs.add(s2);

        Assert.assertEquals(hs.size(), 2);

        //only hashcode
        HashMap<StringAsAttrClassWithHashCode, Integer> map = new HashMap<>();
        map.put(new StringAsAttrClassWithHashCode("1"), 1);
        map.put(new StringAsAttrClassWithHashCode("1"), 1);
        Assert.assertEquals(map.size(), 2);
        Assert.assertEquals(map.entrySet().size(), 2);
        Assert.assertEquals(map.keySet().size(), 2);
        //map contains duplicates by hashcode
        Assert.assertEquals(((StringAsAttrClassWithHashCode) map.keySet().toArray()[0]).str, "1");
        Assert.assertEquals(((StringAsAttrClassWithHashCode) map.keySet().toArray()[1]).str, "1");

        //only equals, it's not enough
        HashMap<StringAsAttrClassWithEquals, Integer> equalsMap = new HashMap<>();
        equalsMap.put(new StringAsAttrClassWithEquals("1"), 1);
        equalsMap.put(new StringAsAttrClassWithEquals("1"), 1);
        Assert.assertEquals(map.size(), 2);
    }

    private class StringAsAttrClassWithHashCode {
        private String str;
        public StringAsAttrClassWithHashCode(String str) {
            this.str = str;
        }
        @Override
        public int hashCode() {
            return this.str.hashCode();
        }
    }

    private class StringAsAttrClassWithEquals {
        private String str;
        public StringAsAttrClassWithEquals(String str) {
            this.str = str;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StringAsAttrClassWithEquals) {
                StringAsAttrClassWithEquals ht = (StringAsAttrClassWithEquals) obj;
                return this.str.equals(ht.str);
            }
            return false;
        }
    }

    private class StringAsAttrClassWithHashCodeAndEquals {
        private String str;
        public StringAsAttrClassWithHashCodeAndEquals(String str) {
            this.str = str;
        }

        //it's not enough, see HashMap#putVal
        @Override
        public int hashCode() {
            return this.str.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof StringAsAttrClassWithHashCodeAndEquals) {
                StringAsAttrClassWithHashCodeAndEquals ht = (StringAsAttrClassWithHashCodeAndEquals) obj;
                return this.str.equals(ht.str);
            }
            return false;
        }
    }

    private class StringAsAttrClass {
        private String str;
        public StringAsAttrClass(String str) {
            this.str = str;
        }
    }
}
