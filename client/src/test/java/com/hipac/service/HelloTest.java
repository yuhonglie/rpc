package com.hipac.service;

import com.hipac.service.rpc.RpcProxy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huqiang on 15/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:client-spring.xml")
public class HelloTest {

    private static final Logger logger= LoggerFactory.getLogger(HelloTest.class);

    @Autowired
    HelloService helloService;

    @Test
    public void test_hello(){
        for (int i = 0; i < 1000; i++) {
            long time=System.currentTimeMillis();
            logger.info("[HelloTest - test_hello ]"+helloService.getClass()+"   "+helloService.sayHello("huq"+i));
            logger.info("执行时间"+i+"   :"+(System.currentTimeMillis()-time));
        }

    }


}
