package com.lautbiru.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static final String BOOKED_DATE_PATTERN = "yyyy-MM-dd";

    public static Date createDate(String date) {
        try {
            return new SimpleDateFormat(BOOKED_DATE_PATTERN).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

//    public static Date testDate() {
//
//    }
}
