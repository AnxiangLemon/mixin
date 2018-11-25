package com.lemon.mixin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lemon.mixin.entity.MyFriends;
import com.lemon.mixin.entity.vo.MyFriendsVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */
public interface MyFriendsMapper extends BaseMapper<MyFriends> {
    List<MyFriendsVO> queryFriendList(String userid);
}
