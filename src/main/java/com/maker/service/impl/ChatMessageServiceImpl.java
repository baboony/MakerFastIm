package com.maker.service.impl;

import com.maker.entity.ChatMessage;
import com.maker.mapper.ChatMessageMapper;
import com.maker.service.ChatMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-25
 */
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatMessageService {

}
