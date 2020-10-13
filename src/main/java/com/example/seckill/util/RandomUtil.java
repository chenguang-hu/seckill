package com.example.seckill.util;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 采用传统的时间戳进行随机数生成
 * 适合并发不是特别大,qps(秒级请求量)为100、1000、10000等情况基本没问题
 */
public class RandomUtil {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");

    // 高负载条件下线程安全, 单例模式
    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    /**
     * 生成订单号
     *
     * @return String
     */
    public static String generateOrderCode() {
        // 时间戳+N为随机数流水号
        return simpleDateFormat.format(DateTime.now().toDate())+generateNumber(4);
    }

    public static String generateNumber(final int num) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 1; i <= num; i++) {
            stringBuffer.append(random.nextInt(9));
        }
        return stringBuffer.toString();
    }

}
