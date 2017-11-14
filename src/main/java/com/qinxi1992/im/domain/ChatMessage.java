package com.qinxi1992.im.domain;

import lombok.Data;

@Data
public class ChatMessage {

    private String username;

    private String nickname;

    private String avatar;

    private String content;

    private String sendTime;
}
