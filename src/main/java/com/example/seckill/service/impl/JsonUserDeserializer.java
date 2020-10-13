package com.example.seckill.service.impl;

import com.example.seckill.entity.User;
import com.example.seckill.service.UserDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonUserDeserializer implements UserDeserializer {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public User deserail(String userData) throws Exception {
        JsonNode rootNode = mapper.readTree(userData);

        String id = rootNode.get("id").textValue();
        if (id == null) {
            return null;
        } else {
            User user = new User(id);
            JsonNode properties = rootNode.get("properties");
            Map<String, Object> propertyMap = mapper.readValue(properties.toString(), HashMap.class);
            user.setProperties(propertyMap);
            return user;
        }

    }
}
