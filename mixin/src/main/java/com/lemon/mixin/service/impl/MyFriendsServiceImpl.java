package com.lemon.mixin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lemon.mixin.entity.MyFriends;
import com.lemon.mixin.mapper.MyFriendsMapper;
import com.lemon.mixin.service.IMyFriendsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@Service
public class MyFriendsServiceImpl extends ServiceImpl<MyFriendsMapper, MyFriends> implements IMyFriendsService {
    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List queryFriendList(String userId) {
       /* Map rule = new HashMap();
        rule.put("my_user_id",userId);*/
       /* QueryWrapper<MyFriends> queryWrapper = new QueryWrapper<MyFriends>();
        List list =  myFriendsMapper.selectList(queryWrapper.eq("my_user_id",userId));*/
       List list = myFriendsMapper.queryFriendList(userId);
        return list;
    }
}
