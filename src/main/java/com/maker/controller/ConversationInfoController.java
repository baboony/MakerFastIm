package com.maker.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.entity.UserInfo;
import com.maker.service.ConversationInfoService;
import com.maker.service.UserInfoService;
import com.maker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/maker/conversationInfo")
@CrossOrigin
public class ConversationInfoController {

    @Autowired
    private ConversationInfoService conversationInfoService;

    @Autowired
    private UserInfoService userInfoService;

    @RequestMapping("getConversationInfo")
    public Result<Page> getConversationInfo(@RequestParam("userId") String userId, @RequestParam(name = "pageNumber", defaultValue = "0")
    int pageNumber, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        //拉取聊天记录（默认10条）
        Page<Map<String, Object>> page = conversationInfoService.getConversationInfoPage(new Page<>(pageNumber, pageSize), userId);
        for (Map<String, Object> record : page.getRecords()) {
            if (Objects.equals(record.get("send_id"), userId)) {
                //如果当前登录的ID和会话列表发送人系统则取接收人信息
                UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", record.get("form_id")));
                record.put("nickName", one.getNickName());
                record.put("faceUrl", one.getFaceUrl());
                record.put("ex", one.getEx());
                record.put("user_id", one.getUserId());
            }
            if (Objects.equals(record.get("form_id"), userId)) {
                //如果当前登录ID和接收人系统则取发送人信息
                UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", record.get("send_id")));
                record.put("nickName", one.getNickName());
                record.put("faceUrl", one.getFaceUrl());
                record.put("ex", one.getEx());
                record.put("user_id", one.getUserId());
            }
        }
//        for (ConversationInfo record : page.getRecords()) {
//            if (Objects.equals(record.getUserId(), userId)) {
//                //如果当前登录的ID和会话列表发送人系统则取接收人信息
//                UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", record.getFormId()));
//            }
//            if (Objects.equals(record.getFormId(), userId)) {
//                //如果当前登录ID和接收人系统则取发送人信息
//                UserInfo one = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("user_id", record.getUserId()));
//
//            }
//        }
        return Result.ok(page);
    }
}

