package com.lemon.mixin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lemon.mixin.entity.ChatMsg;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
public interface ChatMsgMapper extends BaseMapper<ChatMsg> {
    public void updataMsgSigned(List<String> msgIdList);
}
