/*
 * Copyright (C) 2018 Zhejiang xiaominfo Technology CO.,LTD.
 * All rights reserved.
 * Official Web Site: http://www.xiaominfo.com.
 * Developer Web Site: http://open.xiaominfo.com.
 */

package com.yjy.user.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.yjy.user.common.Rest;
import com.yjy.user.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/***
 *
 * @since:swagger-bootstrap-ui 1.0
 * @author <a href="mailto:xiaoymin@foxmail.com">xiaoymin@foxmail.com</a> 
 * 2019/05/04 11:26
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {


    @ApiOperation(value = "查询用户列表")
    @PostMapping(value = "/list")
    public Rest<List<User>> list(){
        Rest<List<User>> rest=new Rest<>();
        List<User> list= CollectionUtil.newArrayList(new User("user1","Java开发工程师","公司2")
        ,new User("user2","C开发工程师","公司1")
        ,new User("user3","JavaScript工程师","公司3")
        ,new User("user4","Ui工程师","公司4")
        ,new User("user5","总经理","公司1"));
        rest.setData(list);
        return rest;
    }

    @ApiOperation(value = "根据用户id查询用户详情")
    @GetMapping("/queryById")
    public Rest<User> queryById(@RequestParam(value = "id") String id){
        Rest<User> userRest=new Rest<>();
        userRest.setData(new User("user5","总经理","公司1"));

        return userRest;
    }





}
