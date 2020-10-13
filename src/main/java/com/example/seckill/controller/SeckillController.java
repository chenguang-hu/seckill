package com.example.seckill.controller;

import com.example.seckill.entity.Order;
import com.example.seckill.exception.SecKillException;
import com.example.seckill.service.PolicySecKillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SeckillController {

    @Resource
    private PolicySecKillService policySecKillService;

    /**
     *
     * @param id 秒杀活动的id
     * @param user_id 当前参与秒杀活动的用户id
     * @return
     */
    @RequestMapping("/seckill")
    @ResponseBody
    public Map processSecKill(Long id, String user_id) {
        Map result = new HashMap();
        try {
            policySecKillService.processSecKill(id, user_id, 1);

            String orderNo = policySecKillService.sendOrderToQueue(user_id);

            Map data = new HashMap();
            data.put("orderNo", orderNo);

            result.put("code", "0");
            result.put("msg", "success");
            result.put("data", data);
        } catch (SecKillException e) {
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }

        return result;
    }

    @RequestMapping("/checkorder")
    public ModelAndView checkOrder(String orderNo, String sku_id) {

        Order order = policySecKillService.checkOrder(orderNo);
        ModelAndView modelAndView = new ModelAndView();

        if (order != null) {
            // 订单已经创建
            modelAndView.addObject("order", order);
            modelAndView.setViewName("/createOrderPage");
        } else {
            modelAndView.addObject("orderNo", orderNo);
            modelAndView.addObject("sku_id", sku_id);
            modelAndView.setViewName("/wait");
        }

        return modelAndView;
    }
}
