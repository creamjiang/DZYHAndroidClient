package com.dld.android.util;

import com.dld.coupon.util.Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    public static boolean beforeCurDate(String paramString) {
        boolean bool = false;
        try {
            Date localDate = Common.DATE_FORMAT.parse(paramString);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(11, 0);
            localCalendar.set(12, 0);
            localCalendar.set(13, -1);
            bool = localDate.before(localCalendar.getTime());
            bool = bool;
            label47: return bool;
        } catch (Exception localException) {
            return bool;
        }
    }

    public static boolean beforeThanOneYear(String paramString) {
        boolean i = false;
        try {
            Date localDate = Common.DATE_FORMAT.parse(paramString);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(1, 1 + localCalendar.get(1));
            localCalendar.set(2, 11);
            localCalendar.set(5, localCalendar.get(5));
            localCalendar.set(11, 0);
            localCalendar.set(12, 0);
            localCalendar.set(13, 0);
            boolean bool = localDate.before(localCalendar.getTime());
            i = bool;
            return i;
        } catch (Exception localException) {
            return i;
        }
    }

    public static boolean compareDate(String paramString1, String paramString2)
            throws ParseException {
        return Common.DATE_FORMAT.parse(paramString1).before(
                Common.DATE_FORMAT.parse(paramString2));
    }

    public static int dateDiff(String paramString1, String paramString2) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        try {
            int i = dateDiff(localSimpleDateFormat.parse(paramString1),
                    localSimpleDateFormat.parse(paramString2));
            i = i;
            return i;
        } catch (Exception localException) {
            // while (true)
            // int i = 0;
            return 0;
        }
    }

    public static int dateDiff(Date paramDate1, Date paramDate2) {
        return (int) ((paramDate1.getTime() - paramDate2.getTime()) / 86400000L);
    }

    public static int dateDiffToday(String paramString) {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");
        Date localDate = new Date();
        try {
            localDate = localSimpleDateFormat.parse(paramString);
            localDate = localDate;
            return dateDiffToday(localDate);
        } catch (ParseException localParseException) {
            while (true)
                localParseException.printStackTrace();
        }
    }

    public static int dateDiffToday(Date paramDate) {
        Calendar localCalendar = Calendar.getInstance();
        localCalendar.set(11, 0);
        localCalendar.set(12, 0);
        localCalendar.set(13, 0);
        return dateDiff(paramDate, localCalendar.getTime());
    }

    public static boolean equalCurDate(String paramString) {
        boolean i = false;
        try {
            Date localDate = Common.DATE_FORMAT.parse(paramString);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(11, 0);
            localCalendar.set(12, 0);
            localCalendar.set(13, 0);
            boolean bool = localDate.toString().equals(
                    localCalendar.getTime().toString());
            i = bool;
            label52: return i;
        } catch (Exception localException) {
            return i;
        }
    }

    public static boolean equalOneYear(String paramString) {
        boolean bool = false;
        try {
            Date localDate = Common.DATE_FORMAT.parse(paramString);
            Calendar localCalendar = Calendar.getInstance();
            localCalendar.set(1, 1 + localCalendar.get(1));
            localCalendar.set(2, 11);
            localCalendar.set(5, localCalendar.get(5));
            localCalendar.set(11, 0);
            localCalendar.set(12, 0);
            localCalendar.set(13, 0);
            bool = localDate.toString().equals(
                    localCalendar.getTime().toString());
            bool = bool;
            label81: return bool;
        } catch (Exception localException) {
            return bool;
        }
    }
}

/*
 * Location: G:\DEV\MobileDev\反编译\classes_dex2jar\ Qualified Name:
 * com.dld.android.util.DateUtils JD-Core Version: 0.6.0
 */