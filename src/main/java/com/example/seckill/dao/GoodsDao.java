package com.example.seckill.dao;

import com.example.seckill.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDao {
    public Goods findById(Long id);

    public List<Goods> findAll();
}
