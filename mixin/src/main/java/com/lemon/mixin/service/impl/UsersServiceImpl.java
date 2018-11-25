package com.lemon.mixin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lemon.mixin.entity.FriendsRequest;
import com.lemon.mixin.entity.MyFriends;
import com.lemon.mixin.entity.Users;
import com.lemon.mixin.enums.SearchFriendsStatusEnum;
import com.lemon.mixin.idworker.Sid;
import com.lemon.mixin.mapper.FriendsRequestMapper;
import com.lemon.mixin.mapper.MyFriendsMapper;
import com.lemon.mixin.mapper.UsersMapper;
import com.lemon.mixin.service.IUsersService;
import com.lemon.mixin.utils.FastDFSClient;
import com.lemon.mixin.utils.FileUtils;
import com.lemon.mixin.utils.QRCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-23
 */
@Service
@Slf4j
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private FriendsRequestMapper friendsRequestMapper;

    @Autowired
    private Sid sid;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private FastDFSClient fastDFSClient;

    //有事务加入当前事务 没有则不创建
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUsernameInfo(String username) {

        QueryWrapper<Users> queryWrapper = new QueryWrapper();
        Users result = usersMapper.selectOne(queryWrapper.eq("username", username));

        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper();

        Map<String, String> map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        Users user = usersMapper.selectOne(queryWrapper.allEq(map));

        return user;
    }

    @Override
    public Users updateUserInfo(Users user) {
        //根据id 去修改用户数据
        int b = usersMapper.updateById(user);
        if (b == 1) {
            user = usersMapper.selectById(user.getId());
        } else {
            log.error("上传图片更新用户数据失败！");
        }
        return user;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer preconditionSearchFriends(String myUserId, String friendUsername) {
        //看看这个朋友id是不是存在
            Users user= queryUsernameInfo(friendUsername);
            if(user==null){
                return SearchFriendsStatusEnum.USER_NOT_EXIST.getStatus();
            }else if(user.getId().equals(myUserId)){
             //添加的是自己
                return SearchFriendsStatusEnum.NOT_YOURSELF.getStatus();
            }

        QueryWrapper<MyFriends> queryWrapper = new QueryWrapper();
        MyFriends result = myFriendsMapper.selectOne(
                queryWrapper.eq("my_user_id", myUserId)
                        .eq("my_friend_user_id",user.getId()));

        //添加的用户已经是你的好友
        if (result!=null){
            return SearchFriendsStatusEnum.ALREADY_FRIENDS.getStatus();
        }


        return SearchFriendsStatusEnum.SUCCESS.getStatus();
    }

    /**
     *
     * @Description 当前没有事务就新建一个事务
     * @Author AnxiangLemon
     * @Date 2018/10/26 17:15
     * @Param [myUserId, friendUsername]
     * @Return void
     */
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void sendFriendRequest(String myUserId, String friendUsername) {
        //根据用户名把朋友信息查询出来
        Users friend = queryUsernameInfo(friendUsername);

        QueryWrapper<FriendsRequest> queryWrapper = new QueryWrapper();
        FriendsRequest result = friendsRequestMapper.selectOne(
                queryWrapper.eq("send_user_id", myUserId)
                        .eq("accept_user_id",friend.getId()));

        //如果查询的结果是空 表示没有好友请求,则发起好友请求
        if(result==null){
            //添加一条好友请求记录
            String requestId = sid.nextShort();

            FriendsRequest friendsRequest = new FriendsRequest();
            friendsRequest.setId(requestId);
            friendsRequest.setSendUserId(myUserId);
            friendsRequest.setAcceptUserId(friend.getId());
            friendsRequest.setRequestDataTime(LocalDateTime.now());

            friendsRequestMapper.insert(friendsRequest);

        }


    }



    /*新用户*/
    @Override
    public boolean save(Users user) {
        String userId = sid.nextShort();

        //为每个用户生成二维码
        //设置本地路径
        String qrCodePath = "F:\\mixinphone\\user" + userId + "qrcode.png";
        //生成二维码图片
        qrCodeUtils.createQRCode(qrCodePath, "mixin://" + user.getUsername());
        //转成mfile
        MultipartFile qrCodeFile = FileUtils.fileToMultipart(qrCodePath);
        String qrCodeUrl = "";
        try {
            qrCodeUrl = fastDFSClient.uploadQRCode(qrCodeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        user.setQrcode(qrCodeUrl);
        user.setId(userId);
        int n = usersMapper.insert(user);
        return n >= 1 ? true : false;
    }

}
