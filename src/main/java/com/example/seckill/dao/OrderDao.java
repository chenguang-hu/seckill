package com.example.seckill.dao;

import com.example.seckill.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao {
    public void insert(Order order);
    public Order findByOrderNo(String orderNo);
}
