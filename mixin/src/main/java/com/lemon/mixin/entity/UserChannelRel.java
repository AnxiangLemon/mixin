package com.lemon.mixin.entity;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 15:14 2018/10/30
 * @ Description: 用户id和channel的关联关系
 */
public class UserChannelRel {
    private static HashMap<String, Channel>manager = new HashMap<>();

    public static void put(String senderId,Channel channel){
        manager.put(senderId,channel);
    }

    public static Channel get(String senderId){
        return manager.get(senderId);
    }

}

