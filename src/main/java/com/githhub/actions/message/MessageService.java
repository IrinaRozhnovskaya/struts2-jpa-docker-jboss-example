package com.githhub.actions.message;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

@Stateless
public class MessageService implements Serializable {

    @Inject
    MessageRepository messageRepository;

    public void sendMessage(final Message sendMessage) {
        if (sendMessage == null) return;

        final String messageBody = sendMessage.getBody();
        if (isNotNullOrBlank(messageBody)) return;

        final String sender = sendMessage.getFrom();
        if (isNotNullOrBlank(sender)) return;

        messageRepository.save(new Message(sender, messageBody));
    }

    private boolean isNotNullOrBlank(String messageBody) {
        if (messageBody == null) return true;
        if (messageBody.trim().equals("")) return true;
        return false;
    }

    public List<Message> findMessagesInDescendOrder() {
        final List<Message> result = new ArrayList<>();
        final List<Message> messagesInDescendOrder = messageRepository.findAllOrderByCreatedAtDesc();
        for (Message message: messagesInDescendOrder) {
            result.add(new Message(message.getFrom(), message.getBody(), message.getCreatedAt()));
        }
        return unmodifiableList(result);
    }
}
