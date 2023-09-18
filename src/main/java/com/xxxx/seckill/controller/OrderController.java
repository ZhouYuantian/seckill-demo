package com.xxxx.seckill.controller;


import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IOrderService;
import com.xxxx.seckill.vo.OrderDetailVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
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
 * @since 2023-08-15
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    IOrderService orderService;

    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user, Long orderId)
    {
        if(orderId==null)
        {
            return RespBean.error(RespBeanEnum.ORDER_NOT_EXIST);
        }
        OrderDetailVo detail=orderService.getDetailById(orderId);
        return RespBean.success(detail);
    }

}
