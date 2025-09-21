package com.certificate.date_format;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateFormatMockTest {
    @Test
    public void dateFormatLongParseTest() {
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.US);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2009);
        c.set(Calendar.MONTH, 6);
        c.set(Calendar.DAY_OF_MONTH, 1);
        //cause July has 6 id in calendar
        Assert.assertEquals(df.format(c.getTime()), "July 1, 2009");
    }
}
