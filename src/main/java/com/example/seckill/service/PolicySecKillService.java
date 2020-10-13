package com.example.seckill.service;

import com.example.seckill.entity.Order;
import com.example.seckill.exception.SecKillException;

public interface PolicySecKillService {
    public void processSecKill(Long id, String userid, Integer num) throws SecKillException;

    public String sendOrderToQueue(String userid);

    public Order checkOrder(String orderNo);
}
