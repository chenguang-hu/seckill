package com.example.seckill.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 用户对象设置为只读
 */
public class User {
    private String id;  // 唯一标识
    private static final Map<String, Object> PROPERTY_MAP = new HashMap<>();

    public String getId() {
        return id;
    }

    public User(String id) {
        this.id = id;
    }

    public void setProperties(Map<String, Object> properties) {
        PROPERTY_MAP.putAll(properties);
    }

    public Object getProperty(String propertyName) {
        return PROPERTY_MAP.get(propertyName);
    }

    public Set<String> propertyNames() {
        return PROPERTY_MAP.keySet();
    }
}
