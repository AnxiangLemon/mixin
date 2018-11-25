package com.lemon.mixin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lemon.mixin.entity.FriendsRequest;
import com.lemon.mixin.entity.MyFriends;
import com.lemon.mixin.entity.UserChannelRel;
import com.lemon.mixin.entity.vo.FriendsRequestVO;
import com.lemon.mixin.enums.MsgActionEnum;
import com.lemon.mixin.idworker.Sid;
import com.lemon.mixin.mapper.FriendsRequestMapper;
import com.lemon.mixin.mapper.MyFriendsMapper;
import com.lemon.mixin.netty.DataContent;
import com.lemon.mixin.service.IFriendsRequestService;
import com.lemon.mixin.utils.JsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@Service
public class FriendsRequestServiceImpl extends ServiceImpl<FriendsRequestMapper, FriendsRequest> implements IFriendsRequestService {
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private MyFriendsMapper myFriendsMapper;

    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<FriendsRequestVO> queryFriendsRequestVOList(String acceptid) {
        List<FriendsRequestVO> list = friendsRequestMapper.queryFriendsRequestVOList(acceptid);
        return list;
    }

    /*删除某个好友请求*/
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void delFriendRequest(String sendUserId, String acceptUserId) {
        QueryWrapper<FriendsRequest> queryWrapper = new QueryWrapper();
        Map<String, String> map = new HashMap();
        map.put("send_user_id", sendUserId);
        map.put("accept_user_id", acceptUserId);
        friendsRequestMapper.delete(queryWrapper.allEq(map));
    }

    @Override
    public void passFriendRequest(String sendUserId, String acceptUserId) {
        //双向添加好友
        saveFriends(sendUserId, acceptUserId);
        saveFriends(acceptUserId, sendUserId);
        //然后删除此记录
        delFriendRequest(sendUserId, acceptUserId);


        //使用websocket 主动推送消息到请求发起者,更新他的通讯录为最新
        Channel sendChannel = UserChannelRel.get(sendUserId);

        if (sendChannel != null) {
            DataContent dataContent = new DataContent();
            dataContent.setAction(MsgActionEnum.PULL_FRIEND.getType());
            sendChannel.writeAndFlush(new TextWebSocketFrame(
                    JsonUtils.objectToJson(dataContent))
            );
        }
    }

    /*保存好友*/
    @Transactional(propagation = Propagation.REQUIRED)
    private void saveFriends(String sendUserId, String acceptUserId) {
        //首先添加一条好友记录
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(acceptUserId);
        myFriends.setMyFriendUserId(sendUserId);
        myFriendsMapper.insert(myFriends);
    }
}
