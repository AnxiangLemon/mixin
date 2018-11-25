package com.lemon.mixin.controller;


import com.lemon.mixin.entity.ChatMsg;
import com.lemon.mixin.service.IChatMsgService;
import com.lemon.mixin.utils.IJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
@RestController
@RequestMapping("/mixin/chatmsg")
public class ChatMsgController {

    @Autowired
    private IChatMsgService chatMsgService;

    @PostMapping("/getUnReadMsgList")
    public IJSONResult getChatMsgList(String acceptUserId){
        if(StringUtils.isEmpty(acceptUserId)){
            return IJSONResult.errorMsg("");
        }
        //查询列表
        List<ChatMsg>list= chatMsgService.queryChatMsgLogin(acceptUserId);
        return IJSONResult.ok(list);
    }

}
