package vn.vnpay.paymentproducer.constant;

import vn.vnpay.paymentproducer.constant.ResponseCode;

public enum BaseResponse implements ResponseCode {
    SUCCESS("00", "Success"),
    INVALID_REQUEST("01", "Invalid request"),
    SYSTEM_ERROR("99", "System error"),
    ;

    BaseResponse(String code, String message) {
        this.code = code;
        this.message = message;
    };

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
