package com.maker.service.impl;

import com.maker.entity.UserInfo;
import com.maker.mapper.UserInfoMapper;
import com.maker.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 王俊程
 * @since 2022-08-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

}
