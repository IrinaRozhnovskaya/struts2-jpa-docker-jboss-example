package com.githhub.actions;

import com.githhub.actions.message.Message;
import com.githhub.actions.message.MessageService;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class IndexAction extends ActionSupport {

    @Inject
    MessageService messageService;

    String from, body;

    public void setFrom(final String from) {
        this.from = from;
    }

    public void setBody(final String body) {
        this.body = body;
    }

    @Override
    @Action("/*")
    public String execute() {
        messageService.sendMessage(new Message(from, body));
        return "index";
    }

    public List<Message> getMessages() {
        return messageService.findMessagesInDescendOrder();
    }
}

