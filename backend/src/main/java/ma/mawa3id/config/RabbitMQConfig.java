package ma.mawa3id.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange:mawa3id.notifications}")
    private String exchangeName;

    @Value("${app.rabbitmq.queue.send:mawa3id.notifications.send}")
    private String queueName;

    @Value("${app.rabbitmq.queue.dlq:mawa3id.notifications.dlq}")
    private String dlqName;

    @Value("${app.rabbitmq.routing-key.send:notification.send}")
    private String routingKey;

    @Bean
    public DirectExchange notificationsExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue notificationsQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName)
                .withArgument("x-dead-letter-routing-key", "notification.dlq")
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(dlqName).build();
    }

    @Bean
    public Binding notificationsBinding(Queue notificationsQueue, DirectExchange notificationsExchange) {
        return BindingBuilder.bind(notificationsQueue).to(notificationsExchange).with(routingKey);
    }

    @Bean
    public Binding dlqBinding(Queue deadLetterQueue, DirectExchange notificationsExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(notificationsExchange).with("notification.dlq");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
