package com.maker.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.entity.ConversationInfo;
import com.maker.mapper.ConversationInfoMapper;
import com.maker.service.ConversationInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-21
 */
@Service
public class ConversationInfoServiceImpl extends ServiceImpl<ConversationInfoMapper, ConversationInfo> implements ConversationInfoService {

    @Autowired
    private ConversationInfoMapper conversationInfoMapper;


    @Override
    public Page<Map<String, Object>> getConversationInfoPage(Page<?> objectPage, String userId) {
        return conversationInfoMapper.getConversationInfoPage(objectPage, userId);
    }
}
