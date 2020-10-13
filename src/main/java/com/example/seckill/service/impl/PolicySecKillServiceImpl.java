package com.example.seckill.service.impl;

import com.example.seckill.dao.OrderDao;
import com.example.seckill.dao.PolicyDao;
import com.example.seckill.entity.Order;
import com.example.seckill.entity.Policy;
import com.example.seckill.exception.SecKillException;
import com.example.seckill.service.PolicySecKillService;
import com.example.seckill.util.RandomUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class PolicySecKillServiceImpl implements PolicySecKillService {

    @Resource
    private PolicyDao policyDao;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private OrderDao orderDao;

    /**
     * @param id      秒杀政策(活动)id
     * @param user_id 参加秒杀的用户id
     * @param num     用户每次限购的数量, 默认为1
     * @throws SecKillException
     */
    @Override
    public void processSecKill(Long id, String user_id, Integer num) throws SecKillException {
        Policy policy = policyDao.findById(id);

        // 秒杀政策不存在
        if (policy == null) {
            throw new SecKillException("秒杀政策不存在");
        }

        if (policy.getStatus() == 0) {
            throw new SecKillException("秒杀活动还没有开始");
        } else if (policy.getStatus() == 2) {
            throw new SecKillException("秒杀活动已经结束");
        }

        // 从左侧弹出, 每次减一个
        Long goodsId = (Long) redisTemplate.opsForList().leftPop("seckill:count:" + policy.getId());

        // goodsId非空代表商品还有剩余
        if (goodsId != null) {

            // TODO:判断该用户是否已经存在过了
            Boolean isExisted = redisTemplate.opsForSet().isMember("seckill:users:" + policy.getId(), user_id);
            if (!isExisted) {
                System.out.println("恭喜您, 抢购到商品了, 快去下单吧");
                redisTemplate.opsForSet().add("seckill:users:" + policy.getId(), user_id);
            } else {

                // 在队列尾部追加一个, 保证数量一致
                redisTemplate.opsForList().rightPush("seckill:count:" + policy.getId(), policy.getSku_id());
                throw new SecKillException("抱歉, 您已经参加过此次活动, 请不要重复抢购");
            }
        } else {
            // goodsId为null, 商品列表为空
            throw new SecKillException("很抱歉, 商品已经被抢完了");
        }
    }

    @Override
    public String sendOrderToQueue(String user_id) {
        System.out.println("准备向队列发送信息");

        // 订单基本信息
        Map data = new HashMap();
        data.put("user_id", user_id);
        String order_no = RandomUtil.generateOrderCode();
        data.put("order_no", order_no);

        // 附加额外订单信息

        // TODO:将上面的信息发送给exchange-order交换机
        rabbitTemplate.convertAndSend("exchange-order", null, data);
        return order_no;
    }

    @Override
    public Order checkOrder(String orderNo) {
        Order order = orderDao.findByOrderNo(orderNo);
        return order;
    }

}
