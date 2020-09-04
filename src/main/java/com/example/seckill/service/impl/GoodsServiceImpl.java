package com.example.seckill.service.impl;

import com.example.seckill.dao.GoodsDao;
import com.example.seckill.entity.Goods;
import com.example.seckill.service.GoodsService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Resource
    private GoodsDao goodsDao;

    // 第一次访问时将方法的返回结果放入缓存
    // 第二次访问时不再执行方法内部的代码, 而是直接从redis中取数据
    @Cacheable(value = "goods", key = "#id")
    @Override
    public Goods getGoods(Long id) {
        return goodsDao.findById(id);
    }

    // 返回商品列表
    @Override
    public List<Goods> getGoodsList() {
        return goodsDao.findAll();
    }
}
