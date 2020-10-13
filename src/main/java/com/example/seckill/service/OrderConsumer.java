package com.example.seckill.service;

import com.example.seckill.dao.OrderDao;
import com.example.seckill.entity.Order;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Component
public class OrderConsumer {
    @Resource
    private OrderDao orderDao;

    // Rabbit监听器, 描述通过哪个交换器的哪个队列进行数据的绑定
    // 这里queue-order队列绑定到了exchange-order交换机
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value = "queue-order"),
                    exchange = @Exchange(value = "exchange-order", type = "fanout")
            )
    )

    /**
     * 处理消息
     * @param data      放入MQ的数据为Map类型(参见PolicySecKillServiceImpl的sendOrderToQueue方法)
     * @param channel   通道对象
     * @param headers   消息头部, 存放一些辅助信息
     */
    // Map data被@Payload描述, 代表消息的主体数据, 必须加该注解, 才能取数据并转换为Map
    // @Headers注解描述头部, 因为使用的Map类型一次获取到所有的头部信息
    @RabbitHandler
    public void handleMessage(@Payload Map data, Channel channel, @Headers Map<String, Object> headers) {
        System.out.println("--------获取到订单数据:"+ data +"--------");

        try {

            // 创建订单之前有很多的额外操作(对接支付宝/微信支付, 对接物流系统, 日志登记......)
            // 这里对其进行简单模拟, 假设上述操作要1秒
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO:消息确认
            Order order = new Order();
            order.setOrder_no(data.get("order_no").toString());
            order.setOrder_status(0);
            order.setUser_id(data.get("user_id").toString());
            order.setRecv_name("somebody");
            order.setRecv_address("somewhere");
            order.setRecv_mobile("15729363691");
            order.setAmount(1000L);
            order.setPostage(0L);
            order.setCreate_time(new Date());
            orderDao.insert(order);

            // TODO:获得消息在当前channel中的id
            Long tag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);

            // 参数false代表不进行批量接收, 只接收单个也就是当前tag指向的消息
            channel.basicAck(tag, false);

            System.out.println(data.get("order_no") + "订单已创建");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
