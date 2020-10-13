package com.example.seckill.controller;

import com.example.seckill.entity.SecGoods;
import com.example.seckill.service.GoodsService;
import com.example.seckill.util.SecResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;


@Controller
public class GoodsController {

    @Resource
    private GoodsService goodsService;

    @RequestMapping("/goods")
    @ResponseBody
    public SecResult getGoodsById(Long gid) {
        return SecResult.ok(goodsService.getGoods(gid));
    }

    @RequestMapping("/getGoodsList")
    @ResponseBody
    public SecResult getGoodsList() {
        return SecResult.ok(goodsService.getGoodsList());
    }

    @RequestMapping("/homepage")
    public ModelAndView moveToHomePage() {
        ModelAndView modelAndView = new ModelAndView("/stockListPage");
        return modelAndView;
    }

    @RequestMapping("/stockDetailPage")
    public ModelAndView moveToSinglePage(Long sku_id) {
        ModelAndView modelAndView = new ModelAndView("/stockDetailPage");
        return modelAndView;
    }

    @RequestMapping("/getSecGoodsList")
    @ResponseBody
    public SecResult getSecGoodsList() {
        return SecResult.ok(goodsService.getSecGoodsList());
    }

    @RequestMapping("/sec_goods")
    @ResponseBody
    public SecResult getSecGoodsById(Long gid) {
        SecGoods secGoods = goodsService.getSecGoods(gid);
        return SecResult.ok(secGoods);
    }

}
