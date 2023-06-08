package vn.vnpay.paymentproducer.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentItem {
    private String qrInfor;
    private String quantity;
    private String note;
}
