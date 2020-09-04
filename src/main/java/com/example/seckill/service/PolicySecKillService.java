package com.example.seckill.service;

import com.example.seckill.exception.SecKillException;

public interface PolicySecKillService {
    public void processSecKill(Long id, String userid, Integer num) throws SecKillException;
}
