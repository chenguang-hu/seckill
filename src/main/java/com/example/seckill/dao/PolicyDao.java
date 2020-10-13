package com.example.seckill.dao;

import com.example.seckill.entity.Policy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PolicyDao {
    List<Policy> findUnStartSeckill();

    List<Policy> findExpireSeckill();

    void update(Policy policy);

    Policy findById(long id);
}
