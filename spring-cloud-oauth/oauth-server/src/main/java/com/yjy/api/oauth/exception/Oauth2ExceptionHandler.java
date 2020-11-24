package com.yjy.api.oauth.exception;

import com.yjy.api.oauth.api.CommonResult;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局处理Oauth2抛出的异常
 * Oauth2的登录认证的默认实现中，很多认证失败的操作都会直接抛出OAuth2Exception异常，可使用@ControllerAdvice处理
 */
@ControllerAdvice
public class Oauth2ExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = OAuth2Exception.class)
    public CommonResult handleOauth2(OAuth2Exception e) {
        return CommonResult.failed(e.getMessage());
    }
}
