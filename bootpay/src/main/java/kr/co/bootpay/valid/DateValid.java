package kr.co.bootpay.valid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateValid {
//    isValidFormat("yyyy-MM-dd", "2017-18-15")
    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }
}
