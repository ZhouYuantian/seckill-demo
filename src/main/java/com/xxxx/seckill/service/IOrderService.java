package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
public interface IOrderService extends IService<Order> {

    OrderDetailVo getDetailById(Long orderId);
}
