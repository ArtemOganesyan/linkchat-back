package com.practiceproject.linkchat_back.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practiceproject.linkchat_back.producerPayloads.ChatMessagePayload;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;

@Service
public class ChatMessageProducer {
    private static final String MESSAGE_QUEUE = "LinkChatQueueChatMessages";
    private static final String HOST = "b-188e46d3-f93f-40b3-aed8-4bb2c372cff3.mq.us-east-2.on.aws";
    private static final int PORT = 5671;
    private static final String USERNAME = "linkchat";
    private static final String PASSWORD = "LinkChat.2025!";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void send(ChatMessagePayload payload) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.useSslProtocol(SSLContext.getDefault());

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            channel.queueDeclare(MESSAGE_QUEUE, true, false, false, null);

            String json = objectMapper.writeValueAsString(payload);

            channel.basicPublish(
                    "",
                    MESSAGE_QUEUE,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    json.getBytes(StandardCharsets.UTF_8)
            );
        }
    }
}
