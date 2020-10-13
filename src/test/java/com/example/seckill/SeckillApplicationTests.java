package com.example.seckill;


import com.example.seckill.entity.User;
import com.example.seckill.util.TokenManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;


@SpringBootTest
class SeckillApplicationTests {


    @Test
    void contextLoads() {


//        RestTemplate restTemplate = new RestTemplate(RestConfig.generateHttpRequestFactory());
//        byte[] result = restTemplate.getForObject("https://127.0.0.1:8082" + "/validate_service?vt=" + vt, byte[].class);
//        String ret = new String(result, "utf-8");
//        System.out.println(ret);

        String vt = "aaec4802-73ef-4aa2-8509-fb25e31eae76";

        try {
            User user = TokenManager.validate(vt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

