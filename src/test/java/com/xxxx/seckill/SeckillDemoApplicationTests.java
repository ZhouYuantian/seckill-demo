package com.xxxx.seckill;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootTest
class SeckillDemoApplicationTests {

    @Autowired
    RedisTemplate redisTemplate;
    @Test
    void testLock() {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        Boolean success=valueOperations.setIfAbsent("k1","v1");
        if(success)
        {
            valueOperations.set("name","xxxx");
            String name=(String) valueOperations.get("name");
            System.out.println("name："+name);
            redisTemplate.delete("k1");
        }
        else
        {
            System.out.println("阻塞");
        }
    }

}
