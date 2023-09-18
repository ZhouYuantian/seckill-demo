package com.xxxx.seckill.config;

import com.xxxx.seckill.exception.AccessLimitException;
import com.xxxx.seckill.exception.StateException;
import com.xxxx.seckill.pojo.User;
import com.xxxx.seckill.service.IUserService;
import com.xxxx.seckill.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    IUserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod=(HandlerMethod) handler;
            AccessLimit accessLimit=handlerMethod.getMethodAnnotation(AccessLimit.class);
            if(accessLimit!=null)
            {
                int second= accessLimit.second();
                int maxCount=accessLimit.maxCount();
                String groupName= accessLimit.groupName();

                String key=groupName+":"+getUser(request).getId();
                ValueOperations valueOperations= redisTemplate.opsForValue();
                Integer count=(Integer) valueOperations.get(key);
                if(null==count)
                {
                    valueOperations.set(key,1,second, TimeUnit.SECONDS);
                }
                else if (count<maxCount)
                {
                    valueOperations.increment(key);
                }
                else
                {
                    throw new AccessLimitException();
                }
            }
            return true;
        }
        return true;
    }

    public User getUser(HttpServletRequest request) throws StateException {
        String userTicket= CookieUtil.getCookieValue(request,"userTicket");
        if(!StringUtils.hasText(userTicket)) throw new StateException();
        User user =(User) redisTemplate.opsForValue().get("user:" + userTicket);
        if(null==user) throw new StateException();
        return userService.getUserByCookie(userTicket);
    }
}
