package com.maker.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.maker.entity.UserInfo;
import com.maker.service.UserInfoService;
import com.maker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-20
 */
@RestController
@RequestMapping("/maker/userInfo")
@CrossOrigin
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("getUserById")
    public Result<UserInfo> getUserById(String userId) {
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", userId));
        return userInfo != null ? Result.ok(userInfo) : Result.fail("用户不存在");
    }

    @RequestMapping("addUser")
    public Result<Void> addUser(UserInfo userInfo) {
        boolean save = userInfoService.save(userInfo);
        return save ? Result.ok() : Result.fail();
    }

    @RequestMapping("modifyUser")
    public Result<Void> modifyUser(UserInfo userInfo) {
        boolean save = userInfoService.updateById(userInfo);
        return save ? Result.ok() : Result.fail();
    }

}

