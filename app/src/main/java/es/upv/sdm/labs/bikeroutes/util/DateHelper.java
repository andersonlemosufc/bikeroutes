package es.upv.sdm.labs.bikeroutes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by anderson on 12/04/2016.
 */
public class DateHelper {

    public static Date createDate(int day, int month, int year, int hour, int minutes){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss");
        Date d = new Date();
        try {
            d = formatter.parse(day+"/"+month+"/"+year+"/"+hour+":"+minutes+":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }

    public static String toFormatString(Date date){
        if(date==null) return "";
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(date);
    }


    public static Date toDate(String str) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return s.parse(str);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String dateToString(Date date){
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yy");
        return s.format(date);
    }


    public static String timeToString(Date date){
        SimpleDateFormat s = new SimpleDateFormat("hh:mm");
        return s.format(date);
    }

    public static String dateToMySQLFormat(Date date){
        SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd");
        return s.format(date);
    }


    public static String timeToMySQLFormat(Date date){
        SimpleDateFormat s = new SimpleDateFormat("HH:mm:ss");
        return s.format(date);
    }
}
