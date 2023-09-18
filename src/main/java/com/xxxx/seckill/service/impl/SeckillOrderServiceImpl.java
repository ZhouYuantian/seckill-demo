package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.SeckillOrderMapper;
import com.xxxx.seckill.pojo.*;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.service.ISeckillGoodsService;
import com.xxxx.seckill.service.ISeckillOrderService;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 秒杀订单表 服务实现类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
@Service
public class SeckillOrderServiceImpl extends ServiceImpl<SeckillOrderMapper, SeckillOrder> implements ISeckillOrderService {

    @Autowired
    IGoodsService goodsService;
    @Autowired
    ISeckillGoodsService seckillGoodsService;
    @Autowired
    IOrderService orderService;
    @Autowired
    SeckillOrderMapper seckillOrderMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    @Transactional
    public Order makeOrder(User user, Goods goods)
    {
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("goods_id",goods.getId());
        queryWrapper.last("FOR UPDATE");
        SeckillGoods seckillGoods=seckillGoodsService.getBaseMapper().selectOne(queryWrapper);

        seckillGoods.setStockCount(seckillGoods.getStockCount()-1);
        seckillGoodsService.updateById(seckillGoods);

        Order order=new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliveryAddrId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(goods.getGoodsPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderService.save(order);

        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderMapper.insert(seckillOrder);

        redisTemplate.opsForValue().set("secKillOrder:"+user.getId()+":"+goods.getId(),seckillOrder);
        return order;
    }

    @Override
    public RespBean getResult(User user, Long goodsId) {
        SeckillOrder seckillOrder=seckillOrderMapper.selectOne(
                new QueryWrapper<SeckillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId));
        if(null==seckillOrder)
        {
            return RespBean.success(0L);
        }
        else
        {
            return RespBean.success(seckillOrder.getOrderId());
        }
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String path=MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisTemplate.opsForValue().set("seckillPath:"+user.getId()+":"+goodsId,path,60, TimeUnit.SECONDS);
        return path;
    }

    @Override
    public Boolean checkPath(String path, User user, Long goodsId) {
        return path.equals(redisTemplate.opsForValue().get("seckillPath:"+user.getId()+":"+goodsId));
    }

    @Override
    public Boolean checkCaptcha(User user, Long goodsId, String captcha) {
        String trueCaptcha=(String) redisTemplate.opsForValue().get("captcha:"+user.getId()+":"+goodsId);
        if(StringUtils.hasText(trueCaptcha) && trueCaptcha.equals(captcha))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
