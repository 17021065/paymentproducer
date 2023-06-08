package vn.vnpay.paymentproducer.utils.validate;

import lombok.extern.slf4j.Slf4j;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

@Slf4j
public class Validator {
    private static final String PHONE_PATTERN = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$|^(\\+\\d{1,3}( )?)?(\\d{3}' '?){2}\\d{3}$|^(\\+\\d{1,3}( )?)?(\\d{3}' '?)(\\d{2}' '?){2}\\d{2}$";

    public static boolean checkPhonePattern(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        return pattern.matcher(phoneNumber).matches();
    }

    public static boolean checkDateFormat(String date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date);
        } catch (ParseException e) {
            log.error("Parse date has an exception: ", e);
            return false;
        }

        return true;
    }
}
