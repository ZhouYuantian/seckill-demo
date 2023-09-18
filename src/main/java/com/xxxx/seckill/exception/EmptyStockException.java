package com.xxxx.seckill.exception;

import com.xxxx.seckill.vo.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmptyStockException extends Exception{
    private RespBeanEnum respBeanEnum;
}
