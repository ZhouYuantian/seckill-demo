package com.xxxx.seckill.rabbitmq;

import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.pojo.SeckillMessage;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    IGoodsService goodsService;
//    @RabbitListener(queues = "queue")
//    public void receive(Object msg)
//    {
//        log.info("接受消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout01")
//    public void receive01(Object msg)
//    {
//        log.info("QUEUE01接收消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_fanout02")
//    public void receive02(Object msg)
//    {
//        log.info("QUEUE02接收消息: "+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct01")
//    public void receive03(Object msg)
//    {
//        log.info("QUEUE01接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_direct02")
//    public void receive04(Object msg)
//    {
//        log.info("QUEUE02接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic01")
//    public void receive05(Object msg)
//    {
//        log.info("QUEUE01接收消息"+msg);
//    }
//
//    @RabbitListener(queues = "queue_topic02")
//    public void receive06(Object msg)
//    {
//        log.info("QUEUE02接收消息"+msg);
//    }
    @RabbitListener(queues = "seckillQueue")
    public void receive(String message)
    {
        log.info("接收消息: "+message);
        SeckillMessage seckillMessage=JsonUtil.jsonStr2Object(message,SeckillMessage.class);

        User user=seckillMessage.getUser();
        Long goodsId=seckillMessage.getGoodsId();
        Goods goods=goodsService.getById(goodsId);

        seckillOrderService.makeOrder(user,goods);
    }
}
