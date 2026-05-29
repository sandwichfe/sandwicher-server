package com.sandwich.wx.config;

import com.lww.common.constant.MqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public DirectExchange wxMsgExchange() {
        return new DirectExchange(MqConstants.WX_MSG_EXCHANGE);
    }

    @Bean
    public Queue wxMsgQueue() {
        return new Queue(MqConstants.WX_MSG_QUEUE, true);
    }

    @Bean
    public Binding wxMsgBinding(Queue wxMsgQueue, DirectExchange wxMsgExchange) {
        return BindingBuilder.bind(wxMsgQueue).to(wxMsgExchange).with(MqConstants.WX_MSG_ROUTING_KEY);
    }
}
