package com.xxxx.seckill.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.seckill.exception.NormalException;
import com.xxxx.seckill.mapper.UserMapper;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.utils.MD5Util;
import com.xxxx.seckill.utils.UUIDUtil;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile=loginVo.getMobile();
        String password=loginVo.getPassword();

        User user=userMapper.selectById(mobile);
        if(null==user) throw new NormalException(RespBeanEnum.LOGIN_ERROR);
        if(!MD5Util.formPassToDbPass(password,user.getSalt()).equals(user.getPassword()))
        {
            throw new NormalException(RespBeanEnum.LOGIN_ERROR);
        }

        String ticket= UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:"+ticket,user);

        //request.getSession().setAttribute(ticket,user);

        CookieUtil.setCookie(request,response,"userTicket",ticket);
        return RespBean.success(ticket);
    }

    @Override
    public User getUserByCookie(String userTicket) {
        User user =(User) redisTemplate.opsForValue().get("user:" + userTicket);
        return user;
    }
}
