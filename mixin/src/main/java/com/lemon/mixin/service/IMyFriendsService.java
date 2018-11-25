package com.lemon.mixin.service;

import com.lemon.mixin.entity.MyFriends;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
public interface IMyFriendsService extends IService<MyFriends> {


    /*查询好友列表*/
    public List queryFriendList(String userId);

}
