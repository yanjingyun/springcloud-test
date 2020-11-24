package com.yjy.api.oauth.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2获取Token返回信息封装
 * 我们将需要的JWT信息封装成对象，然后放入到我们的CommonResult对象的data属性中
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Oauth2TokenDto {
    /**
     * 访问令牌
     */
    private String token;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * 访问令牌头前缀
     */
    private String tokenHead;
    /**
     * 有效时间（秒）
     */
    private int expiresIn;
}
