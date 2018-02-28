package com.pay.test;

import com.ipaylinks.conf.ConfApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes=ConfApplication.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test() throws Exception {
        System.out.println(111);
        stringRedisTemplate.opsForValue().set("aaa", "测试123");
        System.out.println(222);
        System.out.println(stringRedisTemplate.opsForValue().get("aaa"));
        System.out.println(333);

    }

    @Test
    public void testObj() throws Exception {
        String value = "test123";
        ValueOperations<String, String> operations=redisTemplate.opsForValue();
        operations.set("com.neox", value);
        operations.set("com.neo.f", value,1, TimeUnit.SECONDS);
        Thread.sleep(1000);
        //redisTemplate.delete("com.neo.f");
        boolean exists=redisTemplate.hasKey("com.neo.f");
        if(exists){
            System.out.println("exists is true");
        }else{
            System.out.println("exists is false");
        }
        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
    }
}
