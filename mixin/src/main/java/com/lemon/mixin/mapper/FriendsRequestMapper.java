package com.lemon.mixin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lemon.mixin.entity.FriendsRequest;
import com.lemon.mixin.entity.vo.FriendsRequestVO;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-26
 */

public interface FriendsRequestMapper extends BaseMapper<FriendsRequest> {
    List<FriendsRequestVO> queryFriendsRequestVOList(String id);

}
