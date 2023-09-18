package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessLimitException extends Exception
{
    private RespBeanEnum respBeanEnum=RespBeanEnum.ACCESS_LIMIT_REACHED;
}
