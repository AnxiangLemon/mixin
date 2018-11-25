package com.lemon.mixin.service;

import com.lemon.mixin.entity.ChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lemon.mixin.netty.MixinMsg;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
public interface IChatMsgService extends IService<ChatMsg> {
    /*保存消息*/
    public String saveMsg(MixinMsg mixinMsg);

    /*保存msgid 为读*/
    public void updataMsgSigned(List<String>msgIdList);

    /*查询聊天消息列表*/
    public List queryChatMsgLogin(String acceptId);
}
