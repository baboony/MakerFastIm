package com.maker.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.entity.ConversationInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-21
 */
public interface ConversationInfoService extends IService<ConversationInfo> {

    Page<Map<String, Object>> getConversationInfoPage(Page<?> objectPage, String userId);
}
