package com.maker.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.maker.entity.ChatMessage;
import com.maker.service.ChatMessageService;
import com.maker.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

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
    private ChatMessageService chatMessageService;


    @RequestMapping("getHistoricalNews")
    public Result<Page> getHistoricalNews(@RequestParam("userId") String userId, @RequestParam("formId") String formId,@RequestParam(name = "pageNumber", defaultValue = "0")
                                          int pageNumber, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        //拉取聊天记录（默认10条）
        Page<ChatMessage> page = chatMessageService.page(new Page<ChatMessage>(pageNumber, pageSize), new QueryWrapper<ChatMessage>().eq("come", userId).eq("go", formId).or().eq("come", formId).eq("go", userId).orderByDesc("id"));
        //倒序数据
        page.getRecords().sort(Comparator.comparing(obj -> ((ChatMessage) obj).getId()));
        return Result.ok(page);
    }

}

