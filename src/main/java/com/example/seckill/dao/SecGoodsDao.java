package com.example.seckill.dao;

import com.example.seckill.entity.SecGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SecGoodsDao {
    // 秒杀商品列表(商品信息+政策信息)
    List<SecGoods> findSecGoods();

    // 通过id查询单个秒杀商品信息
    SecGoods findSecGoodsByid(Long id);
}
