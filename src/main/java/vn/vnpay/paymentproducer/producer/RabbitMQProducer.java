package vn.vnpay.paymentproducer.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQProducer {
    private final RabbitAdmin rabbitAdmin;

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName = "payment-exchange";
    private final String routingKey = "add-payment";

    public void sendMessage(String message) {
        Exchange exchange = new DirectExchange(exchangeName);
        Queue queue = new Queue(routingKey, true, false, false);
        rabbitAdmin.declareExchange(exchange);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(routingKey).noargs());

        rabbitTemplate.execute(channel -> {
            channel.basicPublish(exchangeName, routingKey, null, message.getBytes());
            return null;
        });
    }
}
