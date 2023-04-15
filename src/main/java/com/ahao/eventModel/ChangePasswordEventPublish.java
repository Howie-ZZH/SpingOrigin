package com.ahao.eventModel;

import org.springframework.context.ApplicationEvent;

public class ChangePasswordEventPublish extends ApplicationEvent {

    public ChangePasswordEventPublish(Object source) {
        super(source);
        System.out.println("Event send success!");
    }
}
