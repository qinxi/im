package com.qinxi1992.im.controller;

import com.alibaba.fastjson.JSON;
import com.qinxi1992.im.domain.ChatMessage;
import com.qinxi1992.im.domain.MyMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class IndexController {

    @Resource
    private SimpMessagingTemplate template;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");

    @GetMapping(value = "/index")
    public String login() {
        return "index";
    }


    @MessageMapping("/all")
    public void all(Principal principal, String message) {
        ChatMessage chatMessage = createMessage(principal.getName(), message);
        template.convertAndSend("/topic/notice", JSON.toJSONString(chatMessage));
    }

    @MessageMapping("/chat")
    public void chat(Principal principal, String message) {
        MyMessage myMessage = JSON.parseObject(message, MyMessage.class);
        myMessage.setSender(principal.getName());
        this.send(myMessage);
    }

    private void send(MyMessage message) {
        message.setDate(new Date());
        ChatMessage chatMessage = createMessage(message.getSender(), message.getContent());
        template.convertAndSendToUser(message.getReceiver(), "/topic/chat", JSON.toJSONString(chatMessage));
    }

    private ChatMessage createMessage(String username, String message) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUsername(username);
        /*chatMessage.setAvatar(user.getAvatar());
        chatMessage.setNickname(user.getNickname());*/
        chatMessage.setContent(message);
        chatMessage.setSendTime(simpleDateFormat.format(new Date()));
        return chatMessage;
    }
}
