package vn.vnpay.paymentproducer.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import vn.vnpay.paymentproducer.bean.PaymentRequestWithItem;
import vn.vnpay.paymentproducer.bean.PaymentResponse;
import vn.vnpay.paymentproducer.constant.BaseResponse;
import vn.vnpay.paymentproducer.constant.Constant;
import vn.vnpay.paymentproducer.producer.RabbitMQProducer;
import vn.vnpay.paymentproducer.service.PaymentService;
import vn.vnpay.paymentproducer.utils.date.DateUtils;
import vn.vnpay.paymentproducer.utils.validate.Validator;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final String tokenKeyHashName = "TOKEN_KEY_LOG";
    private final String traceTransferHashName = "TRACE_TRANSFER_LOG";

    private final RabbitMQProducer rabbitMQProducer;

    @Override
    public PaymentResponse add(PaymentRequestWithItem request) {
        try {
            log.info("Start add payment with request: {}", objectMapper.writeValueAsString(request));

            String today = new SimpleDateFormat(Constant.COMMON_DATE_FORMAT).format(new Date());
            if (!isRequestValid(request, today)) {
                PaymentResponse response = new PaymentResponse(BaseResponse.INVALID_REQUEST);

                log.info("Request is invalid, response: {}", objectMapper.writeValueAsString(response));
                return response;
            }

            log.info("Cache data: hash=[{}], hashKey=[{}], hashValue=[{}]", tokenKeyHashName, request.getTokenKey(), today);
            redisTemplate.opsForHash().put(tokenKeyHashName, request.getTokenKey(), today);

            log.info("Cache data: hash=[{}], hashKey=[{}], hashValue=[{}]", traceTransferHashName, request.getTraceTransfer(), today);
            redisTemplate.opsForHash().put(traceTransferHashName, request.getTraceTransfer(), today);

            redisTemplate.expireAt(tokenKeyHashName, DateUtils.endOfToday());
            redisTemplate.expireAt(traceTransferHashName, DateUtils.endOfToday());

            log.info("RabbitMQ message sent: {}", objectMapper.writeValueAsString(request));
            rabbitMQProducer.sendMessage(objectMapper.writeValueAsString(request));

            PaymentResponse response = new PaymentResponse(BaseResponse.SUCCESS);

            log.info("Finish add payment, response: {}", objectMapper.writeValueAsString(response));
            return response;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRequestValid(PaymentRequestWithItem request, String today) {
        String lastUsedTokenKey = (String) redisTemplate.opsForHash().get(tokenKeyHashName, request.getTokenKey());
        log.info("Last time tokenKey[{}] was used: {}", request.getTokenKey(), lastUsedTokenKey);

        String lastUsedTraceTransfer = (String) redisTemplate.opsForHash().get(traceTransferHashName, request.getTraceTransfer());
        log.info("Last time traceTransfer[{}] was used: {}", request.getTraceTransfer(), lastUsedTraceTransfer);

        if (today.equalsIgnoreCase(lastUsedTokenKey)) {
            log.info("Duplicate tokenKey");
            return false;
        }
        if (request.getApiID() == null || request.getApiID().isBlank()) {
            log.info("Field name apiID is empty");
            return false;
        }
        if (request.getMobile() == null || !Validator.checkPhonePattern(request.getMobile())) {
            log.info("Field name mobile is invalid");
            return false;
        }
        if (request.getBankCode() == null || !Constant.DEFAULT_BANK_CODE.equalsIgnoreCase(request.getBankCode())) {
            log.info("Field name bankCode is invalid");
            return false;
        }
        if (request.getPayDate() == null || !Validator.checkDateFormat(request.getPayDate(), Constant.DEFAULT_DATE_FORMAT)) {
            log.info("Field name payDate has an invalid format");
            return false;
        }
        if (request.getRespCode() == null || !Constant.DEFAULT_RESPONSE_CODE.equalsIgnoreCase(request.getRespCode())) {
            log.info("Field name respCode is invalid");
            return false;
        }
        if (today.equalsIgnoreCase(lastUsedTraceTransfer)) {
            log.info("Duplicate traceTransfer");
            return false;
        }
        if (request.getMessageType() == null || !Constant.DEFAULT_MESSAGE_TYPE.equalsIgnoreCase(request.getMessageType())) {
            log.info("Field name messageType is invalid");
            return false;
        }
        if (request.getOrderCode() == null || request.getOrderCode().isBlank()) {
            log.info("Field name orderCode is empty");
            return false;
        }
        if (request.getRealAmount() > request.getDebitAmount()) {
            log.info("Field name realAmount is greater than debitAmount");
            return false;
        }
        if (request.getRealAmount() != request.getDebitAmount() && (request.getPromotionCode() == null || request.getPromotionCode().isBlank())) {
            log.info("Field name promotionCode is empty");
            return false;
        }
        if (request.getAddValue() == null || !Constant.DEFAULT_ADD_VALUE.equalsIgnoreCase(request.getAddValue())) {
            log.info("Field name apiID is invalid");
            return false;
        }

        log.info("Request is valid");
        return true;
    }
}
