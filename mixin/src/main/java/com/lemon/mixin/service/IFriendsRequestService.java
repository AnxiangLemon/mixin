package com.lemon.mixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lemon.mixin.entity.FriendsRequest;
import com.lemon.mixin.entity.vo.FriendsRequestVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
public interface IFriendsRequestService extends IService<FriendsRequest> {

    /*查询好友请求列表*/
    List<FriendsRequestVO> queryFriendsRequestVOList(String acceptid);

    //删除好友请求
    void delFriendRequest(String sendUserId, String acceptUserId);

    //通过好友请求
    void passFriendRequest(String sendUserId, String acceptUserId);
}
