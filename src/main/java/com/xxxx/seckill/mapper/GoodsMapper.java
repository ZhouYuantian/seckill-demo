package com.xxxx.seckill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.seckill.pojo.Goods;
import com.xxxx.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-15
 */
@Component
public interface GoodsMapper extends BaseMapper<Goods> {

    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
