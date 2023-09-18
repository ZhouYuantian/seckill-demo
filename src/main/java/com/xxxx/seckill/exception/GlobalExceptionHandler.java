package com.xxxx.seckill.exception;


import com.xxxx.seckill.vo.RespBean;
import com.xxxx.seckill.vo.RespBeanEnum;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public RespBean ExceptionHandler(Exception e)
    {
        if (e instanceof BindException)
        {
            BindException ex=(BindException) e;
            RespBean respBean=RespBean.error(RespBeanEnum.BIND_ERROR);
            respBean.setMessage("参数校验异常: "+ex.getBindingResult().getAllErrors().get(0).getDefaultMessage());
            return respBean;
        }
        else if(e instanceof NormalException)
        {
            NormalException ex=(NormalException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }
        else if(e instanceof EmptyStockException)
        {
            EmptyStockException ex=(EmptyStockException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }
        else if (e instanceof AccessLimitException)
        {
            AccessLimitException ex=(AccessLimitException) e;
            return RespBean.error(ex.getRespBeanEnum());
        }
        else
        {
            e.printStackTrace();
            return RespBean.error(RespBeanEnum.ERROR);
        }
    }

    @ExceptionHandler(StateException.class)
    public ModelAndView StateExceptionHandler(Exception e)
    {
        return new ModelAndView("login");
    }
}
