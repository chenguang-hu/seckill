package com.example.seckill.controller;

import com.example.seckill.entity.Goods;
import com.example.seckill.service.GoodsService;
import com.example.seckill.util.SecResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @RequestMapping("/goods")
    @ResponseBody
    public Goods getGoodsById(Long gid) {
//        ModelAndView modelAndView = new ModelAndView("createOrderPage");
        Goods goods = goodsService.getGoods(gid);
//        return modelAndView;
        return goods;
    }

    @RequestMapping("/getGoodsList")
    @ResponseBody
    public SecResult getGoodsList() {
        return SecResult.ok(goodsService.getGoodsList());
    }

    @RequestMapping("/page")
    public ModelAndView moveToPage() {
        ModelAndView modelAndView = new ModelAndView("stockListPage");
        return modelAndView;
    }
}
