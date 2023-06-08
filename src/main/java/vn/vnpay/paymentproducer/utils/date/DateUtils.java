package vn.vnpay.paymentproducer.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    public static Date endOfToday() throws ParseException {
        String today = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String endOfDay = today + "235959";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.parse(endOfDay);
    }
}
