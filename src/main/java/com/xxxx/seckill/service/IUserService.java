package com.xxxx.seckill.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.vo.LoginVo;
import com.xxxx.seckill.vo.RespBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author YuantianZhou
 * @since 2023-08-12
 */
public interface IUserService extends IService<User> {

    RespBean login(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    User getUserByCookie(String userTicket);
}
