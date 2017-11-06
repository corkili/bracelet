package org.bracelet.common.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        System.out.println("0> " + dateFormat.format(calendar.getTime()));
        Date start = new Date();
        Date end = new Date();
        for (int i = 0; i < 7; i++) {
            end.setTime(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, -1);
            start.setTime(calendar.getTimeInMillis());
            System.out.println(i + "> " + dateFormat.format(start) + " ~ " + dateFormat.format(end));
        }
    }
}
