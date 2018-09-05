package com.certificate.serialization;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

//Serializable need to work with ObjectOutputStream
public class SerializationMockTest implements Serializable {

    @Test
    public void testFileOutputStreamWithEmptySerializableNestedClass() throws IOException {
        boolean hasException = false;
        Hotel h = new Hotel();
        try {
            FileOutputStream fos = new FileOutputStream("Hotel.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
            oos.close();
        } catch(Exception ex) {
            hasException = true;
        }
        Assert.assertFalse(hasException);
        String content = IOUtils.toString(new FileInputStream("Hotel.dat"));
        System.out.println(content);
        Assert.assertTrue(content.contains("com.certificate.serialization.SerializationMockTest$Hotel"));
        Assert.assertTrue(content.contains("com.certificate.serialization.SerializationMockTest$Room"));
        Assert.assertTrue(content.contains("com.certificate.serialization.SerializationMockTest"));
    }

    private class Hotel implements Serializable {
        private Room room = new Room();
    }

    private class Room implements Serializable {
    }
}
