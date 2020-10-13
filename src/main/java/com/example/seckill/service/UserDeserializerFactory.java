package com.example.seckill.service;

import com.example.seckill.service.impl.JsonUserDeserializer;

public class UserDeserializerFactory {
    private UserDeserializerFactory() {
    }

    public static UserDeserializer create(){
        return new JsonUserDeserializer();
    }
}
