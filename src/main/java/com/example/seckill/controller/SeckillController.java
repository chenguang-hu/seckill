package com.example.seckill.controller;

import com.example.seckill.exception.SecKillException;
import com.example.seckill.service.PolicySecKillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SeckillController {

    @Resource
    private PolicySecKillService policySecKillService;

    @RequestMapping("/seckill")
    @ResponseBody
    public Map processSecKill(Long id, String user_id) {
        Map result = new HashMap();
        try {
            policySecKillService.processSecKill(id, user_id, 1);
            result.put("code", "0");
            result.put("msg","success");
        } catch (SecKillException e){
            result.put("code", "500");
            result.put("msg", e.getMessage());
        }

        return result;
    }
}
