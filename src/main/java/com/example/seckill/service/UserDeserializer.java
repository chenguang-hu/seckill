package com.example.seckill.service;

import com.example.seckill.entity.User;

public interface UserDeserializer {

    public User deserail(String userData) throws Exception;
}
