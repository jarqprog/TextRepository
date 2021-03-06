package com.jarq.system.helpers.datetimer;

import com.jarq.AbstractTest;

import com.jarq.system.enums.DateTimerFormatter;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class DateTimerTest extends AbstractTest {

    private IDateTimer dateTimer;

    @Before
    public void setUp() {
        dateTimer = DateTimer.getInstance();
    }

    @Test
    public void getInstance() {

        IDateTimer anotherDateTimer = DateTimer.getInstance(DateTimerFormatter.LOGGER);


        assertNotNull(dateTimer);
        assertTrue(dateTimer instanceof DateTimer);

        assertNotNull(anotherDateTimer);
        assertTrue(anotherDateTimer instanceof DateTimer);
    }

    @Test
    public void getCurrentDateTime() {

        //  check if dateTimer returns desired data - proper date time (String)
        //  take samples twice to avoid the influence of the changing hour (unlikely, but still)

        String toCompare1 = getDateTimeToCompare();
        String toTest1 = splitDateTime(dateTimer.getCurrentDateTime());
        String toCompare2 = getDateTimeToCompare();
        String toTest2 = splitDateTime(dateTimer.getCurrentDateTime());

        boolean condition1 = toCompare1.equals(toTest1);
        boolean condition2 = toCompare2.equals(toTest2);

        assertTrue(condition1 || condition2);
    }


    private String getDateTimeToCompare() {

        String formatter = "yyyy-MM-dd HH:mm:ss";

        LocalDateTime toCompare = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern(formatter);
        return splitDateTime(toCompare.format(format));
    }

    private String splitDateTime(String dateTime) {
        String splitPoint = ":";
        int desiredIndex = 0;
        return dateTime.split(splitPoint)[desiredIndex];  // to get only year, month, day & hour

    }
}