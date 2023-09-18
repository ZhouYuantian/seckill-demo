package com.xxxx.seckill.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wf.captcha.ArithmeticCaptcha;
import com.xxxx.seckill.config.AccessLimit;
import com.xxxx.seckill.pojo.*;
import com.xxxx.seckill.rabbitmq.MQSender;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.JsonUtil;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
@RequestMapping("/secKill")
public class SecKillController implements InitializingBean {
    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillGoodsService seckillGoodsService;
    @Autowired
    ISeckillOrderService seckillOrderService;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MQSender mqSender;
    @Autowired
    RedisScript<Long> decStockScript;

    private Set<Long> emptyStockSet=new HashSet<>();

//    //优化前吞吐量:2845
//    @RequestMapping("/doSecKill")
//    public String doSecKill(Model model, User user,Long goodsId)
//    {
//        model.addAttribute("user",user);
//        GoodsVo goods=goodsService.findGoodsByGoodsId(goodsId);
//        if(goods.getStockCount()<1)
//        {
//            model.addAttribute("errMsg", RespBeanEnum.EMPTY_STOCK.getMessage());
//            return "secKillFail";
//        }
//        SeckillOrder seckillOrder=seckillOrderService.getOne(new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId));
//        if(seckillOrder!=null)
//        {
//            model.addAttribute("errMsg",RespBeanEnum.REPEAETE_ERROR.getMessage());
//            return "secKillFail";
//        }
//
//        Order order=seckillOrderService.makeOrder(user,goods);
//        model.addAttribute("order",order);
//        model.addAttribute("goods",goods);
//        return "orderDetail";
//    }

    //优化前吞吐量:2845
    //页面静态化+用户:订单缓存:5817
    //缓存+异步+内存标记: 7000
    @AccessLimit(second=5,maxCount=1,groupName="doSecKill")
    @RequestMapping("/{path}/doSecKill")
    @ResponseBody
    public RespBean doSecKill(@PathVariable String path, User user, Long goodsId)
    {
        ValueOperations valueOperations=redisTemplate.opsForValue();

        Boolean check=seckillOrderService.checkPath(path,user,goodsId);
        if(!check)
        {
            return RespBean.error(RespBeanEnum.REQUEST_ILLEGAL);
        }
        if(emptyStockSet.contains(goodsId))
        {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillOrder seckillOrder=(SeckillOrder) valueOperations.get("secKillOrder:"+user.getId()+":"+goodsId);
        if(seckillOrder!=null)
        {
            return RespBean.error(RespBeanEnum.REPEAETE_ERROR);
        }

        Long stockCount=(Long) redisTemplate.execute(decStockScript,Collections.singletonList("seckillGoodsCount:"+goodsId),Collections.EMPTY_LIST);
        if(stockCount<=0)
        {
            emptyStockSet.add(goodsId);
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }

        SeckillMessage seckillMessage=new SeckillMessage(user,goodsId);
        mqSender.sendSeckillMessage(JsonUtil.object2JsonStr(seckillMessage));
        return RespBean.success();
    }

    @RequestMapping("getResult")
    @ResponseBody
    public RespBean getResult(User user,Long goodsId)
    {
        return seckillOrderService.getResult(user,goodsId);
    }


    @RequestMapping("getPath")
    @ResponseBody
    public RespBean getPath(User user,Long goodsId,String captcha)
    {
        Boolean check=seckillOrderService.checkCaptcha(user,goodsId,captcha);
        if(!check) return RespBean.error(RespBeanEnum.ERROR_CAPTCHA);
        String path=seckillOrderService.createPath(user,goodsId);
        return RespBean.success(path);
    }

    @RequestMapping(value = "/captcha")
    public void getCode(User user, Long goodsId, HttpServletResponse response)
    {
        response.setContentType("image/jpg");
        response.setHeader("Pargm","No-cache");
        response.setHeader("Cache-Control","no-chache");
        response.setDateHeader("Expires",0);

        ArithmeticCaptcha captcha=new ArithmeticCaptcha(130,32,3);
        redisTemplate.opsForValue().set("captcha:"+user.getId()+":"+goodsId,captcha.text(),300, TimeUnit.SECONDS);
        try{
            captcha.out(response.getOutputStream());
        }catch (IOException e) {
            log.error("验证码生成失败",e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ValueOperations valueOperations=redisTemplate.opsForValue();
        List<SeckillGoods> seckillGoodsList=seckillGoodsService.list();
        for(SeckillGoods seckillGoods:seckillGoodsList)
        {
            valueOperations.set("seckillGoodsCount:"+seckillGoods.getGoodsId(),seckillGoods.getStockCount());
        }

    }
}
