package com.xxxx.seckill.vo;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum RespBeanEnum {
    SUCCESS(200,"SUCCESS"),
    ERROR(500,"服务端异常"),
    LOGIN_ERROR(500200,"用户名或密码错误"),
    MOBILE_ERROR(500211,"手机号错误"),
    BIND_ERROR(500212,"参数校验异常"),
    EMPTY_STOCK(500500,"库存不足"),
    REPEAETE_ERROR(500501,"该商品每人限购一件"),
    ORDER_NOT_EXIST(500300,"订单信息不存在"),
    ORDER_FAIL(500301,"下单失败"),
    REQUEST_ILLEGAL(500502,"请求非法，请重试"),
    ERROR_CAPTCHA(500503,"验证码错误"),
    ACCESS_LIMIT_REACHED(500504,"操作过于频繁，请稍后再试");

    private final Integer code;
    private final String message;
}
