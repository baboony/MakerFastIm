package com.maker.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.entity.MessageInfo;
import com.maker.entity.UserInfo;
import com.maker.service.MessageInfoService;
import com.maker.service.UserInfoService;
import com.maker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-21
 */
@RestController
@RequestMapping("/maker/messageInfo")
@CrossOrigin
public class MessageInfoController {

    @Autowired
    private MessageInfoService messageInfoService;


    @RequestMapping("getHistoricalNews")
    public Result<Page> getHistoricalNews(@RequestParam("userId") String userId, @RequestParam("formId") String formId,@RequestParam(name = "pageNumber", defaultValue = "0")
                                          int pageNumber, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        //拉取聊天记录（默认10条）
        Page<MessageInfo> page = messageInfoService.page(new Page<MessageInfo>(pageNumber, pageSize), new QueryWrapper<MessageInfo>().eq("send_id", userId).eq("form_id", formId).or().eq("send_id", formId).eq("form_id", userId).orderByDesc("id"));
        //倒序数据
        page.getRecords().sort(Comparator.comparing(obj -> ((MessageInfo) obj).getId()));
        return Result.ok(page);
    }

}

