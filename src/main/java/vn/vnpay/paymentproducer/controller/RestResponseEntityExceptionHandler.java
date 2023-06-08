package vn.vnpay.paymentproducer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.vnpay.paymentproducer.constant.BaseResponse;
import vn.vnpay.paymentproducer.bean.PaymentResponse;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<PaymentResponse> handleException(RuntimeException ex, WebRequest request) {
        log.error("There was an error with the request: {}", request, ex);
        PaymentResponse response = new PaymentResponse(BaseResponse.SYSTEM_ERROR);
        return ResponseEntity.ok(response);
    }
}
