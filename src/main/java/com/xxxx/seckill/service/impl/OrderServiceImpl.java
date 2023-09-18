package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.mapper.OrderMapper;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.service.IGoodsService;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.GoodsVo;
import com.xxxx.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    IGoodsService goodsService;
    @Override
    public OrderDetailVo getDetailById(Long orderId) {
        OrderDetailVo orderDetail=new OrderDetailVo();
        Order order=orderMapper.selectById(orderId);
        GoodsVo goodsVo=goodsService.findGoodsByGoodsId(order.getGoodsId());
        orderDetail.setOrder(order);
        orderDetail.setGoodsVo(goodsVo);
        return orderDetail;
    }
}
