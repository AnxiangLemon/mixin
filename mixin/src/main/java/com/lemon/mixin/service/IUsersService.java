package com.lemon.mixin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lemon.mixin.entity.Users;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-23
 */
public interface IUsersService extends IService<Users> {

    /*判断用户名是否存在*/
    public Users queryUsernameInfo(String username);
    /*检查用户名和密码*/
    public Users queryUserForLogin(String username, String password);

    /*修改用户信息 返回更新后的用户 */
    public  Users updateUserInfo(Users user);

    /*搜索好友 返回对应的key*/
    public Integer preconditionSearchFriends(String myUserId,String friendUsername);

    /*查询好友请求*/
   public void sendFriendRequest(String myUserId, String friendUsername);

}
