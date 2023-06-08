package vn.vnpay.paymentproducer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.vnpay.paymentproducer.constant.BaseResponse;
import vn.vnpay.paymentproducer.bean.PaymentRequestWithItem;
import vn.vnpay.paymentproducer.bean.PaymentResponse;
import vn.vnpay.paymentproducer.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity<PaymentResponse> addPaymentTransaction(@RequestBody PaymentRequestWithItem request) {
        PaymentResponse response = paymentService.add(request);

        return ResponseEntity.ok(response);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<PaymentResponse> handleException(Exception e) {
        log.error("System error", e);

        return ResponseEntity.ok(new PaymentResponse(BaseResponse.SYSTEM_ERROR));
    }
}
