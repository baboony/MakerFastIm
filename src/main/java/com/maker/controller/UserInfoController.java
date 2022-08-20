package com.maker.controller;


import com.maker.entity.UserInfo;
import com.maker.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    @RequestMapping("addUser")
    public Integer addUser(UserInfo userInfo) {
        boolean save = userInfoService.save(userInfo);
        return save ? 0 : 101;
    }

    @RequestMapping("modifyUser")
    public Integer modifyUser(UserInfo userInfo) {
        boolean save = userInfoService.updateById(userInfo);
        return save ? 0 : 101;
    }

}

