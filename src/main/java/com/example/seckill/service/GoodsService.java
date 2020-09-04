package com.example.seckill.service;

import com.example.seckill.entity.Goods;

import java.util.List;

public interface GoodsService {
    public Goods getGoods(Long id);

    public List<Goods> getGoodsList();
}
