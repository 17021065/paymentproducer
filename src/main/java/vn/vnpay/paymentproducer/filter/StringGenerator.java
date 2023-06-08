package vn.vnpay.paymentproducer.filter;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.stereotype.Component;

@Component
public class StringGenerator {
    public String generateNewId() {
        return NanoIdUtils.randomNanoId();
    }
}
