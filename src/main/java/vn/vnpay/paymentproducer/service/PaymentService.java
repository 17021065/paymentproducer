package vn.vnpay.paymentproducer.service;

import vn.vnpay.paymentproducer.bean.PaymentRequestWithItem;
import vn.vnpay.paymentproducer.bean.PaymentResponse;

public interface PaymentService {
    PaymentResponse add(PaymentRequestWithItem request);
}
