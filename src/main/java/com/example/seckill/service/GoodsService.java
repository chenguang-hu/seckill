package com.example.seckill.service;

import com.example.seckill.entity.Goods;
import com.example.seckill.entity.SecGoods;

import java.util.List;

public interface GoodsService {
    public Goods getGoods(Long id);

    public List<Goods> getGoodsList();

    public List<SecGoods> getSecGoodsList();

    public SecGoods getSecGoods(Long id);
}
