package com.xxxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.SeckillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 秒杀商品表 Mapper 接口
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
@Component
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

}
