package com.ahao.eventModel;

import org.springframework.stereotype.Component;

@Component
public class EventListener {

    @org.springframework.context.event.EventListener
    public void listenEvent(ChangePasswordEventPublish changePasswordEventPublish) {
        System.out.println("监听到事件：" + changePasswordEventPublish.toString());

    }
}
