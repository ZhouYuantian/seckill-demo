package com.xxxx.seckill.validator;

import com.xxxx.seckill.utils.ValidatorUtil;
import com.xxxx.seckill.validator.IsMobile;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required=false;

    @Override
    public void initialize(IsMobile constraintAnnotation)
    {
        required=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if(required)
        {
            return ValidatorUtil.isMobile(value);
        }
        else
        {
            if(StringUtils.hasText(value))
            {
                return ValidatorUtil.isMobile(value);
            }else
            {
                return true;
            }
        }
    }
}
