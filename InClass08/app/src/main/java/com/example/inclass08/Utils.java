package com.example.inclass08;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;

public class Utils {
    public  static  class HelperFunctions
    {
        public static String getDate()
        {
            DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
            String dateString2 = dateFormat2.format(new Date()).toString();
            return dateString2;
        }
    }
}




