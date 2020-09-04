package com.example.seckill.scheduler;

import com.example.seckill.dao.PolicyDao;
import com.example.seckill.entity.Policy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class SecKillTask {

    @Resource
    private PolicyDao policyDao;

    // Spring封装的redis操作类, 提供了一系列操作redis的模板方法
    @Resource
    private RedisTemplate redisTemplate;

    // cron表达式: 从0秒开始后每5秒钟执行一次下面的方法来检查现在有没有活动可以开始了
    @Scheduled(cron = "0/5 * * * * ?")
    public void startSecKill() {

        List<Policy> list = policyDao.findUnStartSeckill();
        for (Policy policy : list) {
            System.out.println(policy.getId() + "秒杀活动已启动");

            // 删除之前重复的活动任务缓存
            redisTemplate.delete("seckill:count:" + policy.getId());

            // 有几个库存商品, 则初始化几个list对象, 生成一个队列, 从右侧插入, 当有用户抢到商品时, 从左边弹出
            for (int i = 0; i < policy.getQuantity(); i++) {
                redisTemplate.opsForList().rightPush("seckill:count:" + policy.getId(), policy.getSku_id());
            }

            policy.setStatus(1);
            policyDao.update(policy);
        }

    }
}
