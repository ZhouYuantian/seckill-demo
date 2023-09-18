package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-12
 */
@RestController
@RequestMapping("/user")
public class UserController {
//    @Autowired
//    private MQSender mqSender;
//
//    @RequestMapping("/info")
//    @ResponseBody
//    public RespBean info(User user)
//    {
//        return RespBean.success(user);
//    }
//
//    @RequestMapping("/mq")
//    @ResponseBody
//    public void mq()
//    {
//        mqSender.send("Hello");
//    }
//
//    @RequestMapping("/mq/fanout")
//    @ResponseBody
//    public void mq01()
//    {
//        mqSender.send("Hello");
//    }
//
//    @RequestMapping("/mq/direct01")
//    public void mq02()
//    {
//        mqSender.send01("Hello,red");
//    }
//
//    @RequestMapping("/mq/direct02")
//    public void mq03()
//    {
//        mqSender.send02("Hello,green");
//    }
//
//    @RequestMapping("/mq/topic01")
//    public void mq04()
//    {
//        mqSender.send03("Hello,Red");
//    }
//
//    @RequestMapping("/mq/topic02")
//    public void mq05()
//    {
//        mqSender.send04("Hello,Green");
//    }
}
