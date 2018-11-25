package com.lemon.mixin.entity.vo;

import lombok.Data;

/**
 * @ Author     : AnxiangLemon
 * @ Date       : Created in 19:26 2018/10/26
 * @ Description: 好友请求发送方的消息
 */
@Data
public class FriendsRequestVO {
    private String sendUserId;
    private String sendUsername;
    private String sendFaceImage;
    private String sendNickname;
}
