package com.xxxx.seckill.config;

import com.xxxx.seckill.exception.NormalException;
import com.xxxx.seckill.exception.StateException;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@Component
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    IUserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> clazz=parameter.getParameterType();
        return clazz== User.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
    {
        HttpServletRequest request=webRequest.getNativeRequest(HttpServletRequest.class);
        String cookie= CookieUtil.getCookieValue(request,"userTicket");
        if(!StringUtils.hasText(cookie)) throw new StateException();
        User user=userService.getUserByCookie(cookie);
        if(null==user)  throw new StateException();
        return user;
    }
}
