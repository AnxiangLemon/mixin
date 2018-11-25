package com.lemon.mixin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lemon.mixin.entity.ChatMsg;
import com.lemon.mixin.enums.MsgSignFlagEnum;
import com.lemon.mixin.idworker.Sid;
import com.lemon.mixin.mapper.ChatMsgMapper;
import com.lemon.mixin.netty.MixinMsg;
import com.lemon.mixin.service.IChatMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@Service
public class ChatMsgServiceImpl extends ServiceImpl<ChatMsgMapper, ChatMsg> implements IChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private Sid sid;

    /*保存消息到数据库 返回msgId*/
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveMsg(MixinMsg mixinMsg) {

        String msgId  =  sid.nextShort();

        ChatMsg msg = new ChatMsg();

        msg.setId(msgId);
        msg.setAcceptUserId(mixinMsg.getReceiverId());
        msg.setSendUserId(mixinMsg.getSenderId());
        msg.setMsg(mixinMsg.getMsg());
        msg.setCreateTime(LocalDateTime.now());
        msg.setSignFlag(MsgSignFlagEnum.unsign.getType());

        chatMsgMapper.insert(msg);

        return msgId;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updataMsgSigned(List<String> msgIdList) {
        chatMsgMapper.updataMsgSigned(msgIdList);
    }


    /*查询消息列表*/
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List queryChatMsgLogin(String acceptId) {
        QueryWrapper<ChatMsg> queryWrapper = new QueryWrapper();

        Map<String, Object> map = new HashMap();
        map.put("signFlag", 0);
        map.put("acceptUserId", acceptId);
        List<ChatMsg>list = chatMsgMapper.selectList(queryWrapper.allEq(map));


        return list;
    }

}
