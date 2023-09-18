package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.pojo.Order;
import com.xxxx.seckill.pojo.SeckillOrder;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.RespBean;

/**
 * <p>
 * 秒杀订单表 服务类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
public interface ISeckillOrderService extends IService<SeckillOrder> {

    Order makeOrder(User user, Goods goods);

    RespBean getResult(User user, Long goodsId);

    String createPath(User user, Long goodsId);

    Boolean checkPath(String path, User user, Long goodsId);

    Boolean checkCaptcha(User user, Long goodsId, String captcha);
}
