package com.maker.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.maker.entity.ConversationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-21
 */
public interface ConversationInfoMapper extends BaseMapper<ConversationInfo> {

    @MapKey("id")
    Page<Map<String, Object>> getConversationInfoPage(Page<?> objectPage, @Param("userId") String userId);
}
