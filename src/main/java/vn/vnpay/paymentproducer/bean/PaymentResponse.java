package vn.vnpay.paymentproducer.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.vnpay.paymentproducer.constant.BaseResponse;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private String code;
    private String message;

    public PaymentResponse(BaseResponse baseResponse) {
        this.code = baseResponse.getCode();
        this.message = baseResponse.getMessage();
    }
}
