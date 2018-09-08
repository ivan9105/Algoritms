package com.certificate.date_time;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

public class DateTimeMockTest {
    @Test
    public void testMoscowTimezone() {
        TimeZone moscowTimezone = TimeZone.getTimeZone("Europe/Moscow");
        //10800000ms == 3hours, it means +3 hours
        assertEquals(moscowTimezone.getRawOffset(), 10800000);
        //inDaylightTime(new Date(date)) then return getRawOffset() + getDSTSavings()
        //getDSTSavings() + 3600000ms = 1 hours
        assertEquals(moscowTimezone.getOffset(System.currentTimeMillis()), 10800000);
        //использует ли летнее время
        Assert.assertFalse(moscowTimezone.useDaylightTime());

        //separate names for daylight true/false
        assertEquals(moscowTimezone.getDisplayName(false, TimeZone.LONG, Locale.ENGLISH), "Moscow Standard Time");
        assertEquals(moscowTimezone.getDisplayName(false, TimeZone.SHORT, Locale.ENGLISH), "MSK");
        assertEquals(moscowTimezone.getDisplayName(true, TimeZone.LONG, Locale.ENGLISH), "Moscow Daylight Time");
        assertEquals(moscowTimezone.getDisplayName(true, TimeZone.SHORT, Locale.ENGLISH), "MSD");
        assertEquals(moscowTimezone.getDisplayName(false, TimeZone.LONG, Locale.FRENCH), "Heure standard de Moscou");
        assertEquals(moscowTimezone.getDisplayName(false, TimeZone.SHORT, Locale.FRENCH), "MSK");
        assertEquals(moscowTimezone.getDisplayName(true, TimeZone.LONG, Locale.FRENCH), "Heure avancée de Moscou");
        assertEquals(moscowTimezone.getDisplayName(true, TimeZone.SHORT, Locale.FRENCH), "MSD");
    }

    @Test
    public void testGmt5() {
        TimeZone gmt5Timezone = TimeZone.getTimeZone("GMT+5");
        //18000000ms = 5hours
        assertEquals(gmt5Timezone.getRawOffset(), gmt5Timezone.getOffset(System.currentTimeMillis()));
        Assert.assertFalse(gmt5Timezone.useDaylightTime());
        //same time for all locales
        assertEquals(gmt5Timezone.getDisplayName(false, TimeZone.LONG, Locale.FRENCH), gmt5Timezone.getDisplayName(true, TimeZone.SHORT, Locale.UK));
    }

    @Test
    public void testWinterTime() {
        TimeZone moscowTimezone = TimeZone.getTimeZone("Europe/Moscow");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(moscowTimezone);

        //in 2005 was daylight time
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(2005, Calendar.OCTOBER, 29, 22, 0, 0);

        for (int i = 0; i < 62; i++) {
            String mark = dateFormat.format(calendar.getTime());
            System.out.printf("%s - %d, %s\n", mark,
                    moscowTimezone.getOffset(calendar.getTimeInMillis()),
                    moscowTimezone.inDaylightTime(calendar.getTime()));
            calendar.add(Calendar.MINUTE, +1);
        }
    }

    @Test
    public void testSummerTime() {
        TimeZone moscowTimezone = TimeZone.getTimeZone("Europe/Moscow");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(moscowTimezone);

        //in 2005 was daylight time
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(2005, Calendar.MARCH, 26, 22, 0, 0);

        for (int i = 0; i <= 60; i++) {
            String mark = dateFormat.format(calendar.getTime());
            System.out.printf("%s - %d, %s\n", mark,
                    moscowTimezone.getOffset(calendar.getTimeInMillis()),
                    moscowTimezone.inDaylightTime(calendar.getTime()));

            calendar.add(Calendar.MINUTE, +1);
        }
    }

    @Test
    public void testMissing() {
        TimeZone moscowTimezone = TimeZone.getTimeZone("Europe/Moscow");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(moscowTimezone);

        boolean hasException = false;
        Date moment = null;
        try {
            moment = dateFormat.parse("2005-03-27 02:30:00");
        } catch (ParseException e) {
            hasException = true;
        }
        //cause in 2005 move on summer time
        //2005-03-27 01:59:00 MSK - 10800000, false is winter time
        //2005-03-27 03:00:00 MSD - 14400000, true is summer time
        Assert.assertTrue(hasException);
    }

    @Test
    public void testWinterDay() {

    }
}
