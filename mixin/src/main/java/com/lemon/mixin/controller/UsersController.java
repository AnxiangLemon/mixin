package com.lemon.mixin.controller;

import com.lemon.mixin.entity.Users;
import com.lemon.mixin.entity.bo.UserBO;
import com.lemon.mixin.entity.vo.MyFriendsVO;
import com.lemon.mixin.entity.vo.UsersVO;
import com.lemon.mixin.enums.OperatorFriendRequestTypeEnum;
import com.lemon.mixin.enums.SearchFriendsStatusEnum;
import com.lemon.mixin.service.IFriendsRequestService;
import com.lemon.mixin.service.IMyFriendsService;
import com.lemon.mixin.service.IUsersService;
import com.lemon.mixin.utils.FastDFSClient;
import com.lemon.mixin.utils.FileUtils;
import com.lemon.mixin.utils.IJSONResult;
import com.lemon.mixin.utils.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author AnxiangLemon
 * @since 2018-10-23
 */
@RestController
@RequestMapping("/mixin/users")
@Slf4j
public class UsersController  {
    @Autowired
    private IUsersService userService;
    @Autowired
    private IFriendsRequestService friendsRequestService;
    @Autowired
    private IMyFriendsService myFriendsService;

    @Autowired
    private FastDFSClient fastDFSClient;

    /*注册and登录*/
    @PostMapping("/registOrLogin")
    public IJSONResult registOrLogin(@RequestBody Users users){

        //判断用户名和密码不能为空
        if(StringUtils.isEmpty(users.getUsername())||StringUtils.isEmpty(users.getPassword())){
            return IJSONResult.errorMsg("用户名和密码不能为空!");
        }

        //判断用户名是否存在 如果存在就登录 不存在就注册
        Users userResult =   userService.queryUsernameInfo(users.getUsername());

        if(userResult !=null){
            //登录
            userResult = userService.queryUserForLogin(users.getUsername(),MD5Utils.md5(users.getPassword()));
            if(userResult==null){
                return IJSONResult.errorMsg("用户名或密码不正确！");
            }
        }else {
            //注册
            users.setNickname(users.getUsername());
            users.setFaceImage("");
            users.setFaceImageBig("");
            users.setPassword(MD5Utils.md5(users.getPassword()));
            boolean b = userService.save(users);
            if(b){
                userResult = users;

            }else{
                log.info("注册失败！");
                //抛出异常
            }

        }
        UsersVO usersVO =  new UsersVO();
        BeanUtils.copyProperties(userResult,usersVO);

        return IJSONResult.ok(usersVO);
    }

    /*上传头像*/
    @PostMapping("/uploadFaceBase64")
    public IJSONResult registOrLogin(@RequestBody UserBO userBO) throws Exception {

        //获取前端传过来的base64字符串，然后转换为文件对象再上传
        String base64Data = userBO.getFaceData();
        String userFacePath ="F:\\mixinphone\\"+userBO.getUserId()+"userface64.png";
        FileUtils.base64ToFile(userFacePath,base64Data);

        //上传文件到fastdfs
        MultipartFile faceFile = FileUtils.fileToMultipart(userFacePath);
        String url = fastDFSClient.uploadBase64(faceFile);
        System.out.println(url);

        //获取缩略图的url
        String thump = "_80x80.";
        String arr[] = url.split("\\.");
        String thumpImgUrl = arr[0]+thump+arr[1];


        Users user= new Users();
        user.setId(userBO.getUserId());
        user.setFaceImageBig(url);
        user.setFaceImage(thumpImgUrl);

        //更新用户对象
       user = userService.updateUserInfo(user);

        UsersVO usersVO =  new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        return IJSONResult.ok(usersVO);
    }


/*设置昵称*/
    @PostMapping("/setNickName")
    public IJSONResult setNickName(@RequestBody UserBO userBO) throws Exception {

        System.out.println(userBO.toString());
        Users user = new Users();
        user.setId(userBO.getUserId());
        user.setNickname(userBO.getNickname());

        //更新用户对象
        user = userService.updateUserInfo(user);
        UsersVO usersVO =  new UsersVO();
        BeanUtils.copyProperties(user,usersVO);
        return IJSONResult.ok(usersVO);
    }

    /*搜索好友*/
    @PostMapping("/search")
    public IJSONResult searchUser(String myUserId, String friendUsername)  throws Exception {
        //判断传入参数不能为空
        if(StringUtils.isEmpty(myUserId)||StringUtils.isEmpty(friendUsername)){
            return IJSONResult.errorMsg("参数错误");
        }
        Integer status = userService.preconditionSearchFriends(myUserId,friendUsername);
        if(status == SearchFriendsStatusEnum.SUCCESS.getStatus()){
            Users user = userService.queryUsernameInfo(friendUsername);
            UsersVO usersVO =  new UsersVO();
            BeanUtils.copyProperties(user,usersVO);
            return IJSONResult.ok(usersVO);

        }else {
                return  IJSONResult.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }

    }

    /*添加好友请求*/
    @PostMapping("/addFriendRequest")
    public IJSONResult addFriendRequest(String myUserId, String friendUsername)  throws Exception {
        //判断传入参数不能为空
        if(StringUtils.isEmpty(myUserId)||StringUtils.isEmpty(friendUsername)){
            return IJSONResult.errorMsg("参数错误");
        }
        Integer status = userService.preconditionSearchFriends(myUserId,friendUsername);
        if(status == SearchFriendsStatusEnum.SUCCESS.getStatus()){
            userService.sendFriendRequest(myUserId,friendUsername);
            return IJSONResult.ok();
        }else {
            return  IJSONResult.errorMsg(SearchFriendsStatusEnum.getMsgByKey(status));
        }
    }


    /*查询收到的好友请求*/
    @PostMapping("/queryFriendRequest")
    public IJSONResult queryFriendRequest(String userId)  throws Exception {
        //判断传入参数不能为空
        if(StringUtils.isEmpty(userId)){
            return IJSONResult.errorMsg("参数错误");
        }
        return IJSONResult.ok(friendsRequestService.queryFriendsRequestVOList(userId));
    }


    /*操作好友请求 同意或者忽略*/
    @PostMapping("/operFriendRequest")
    public IJSONResult operFriendRequest(String acceptUserId,String sendUserId,Integer operType)  throws Exception {
        //判断传入参数不能为空
        if(StringUtils.isEmpty(acceptUserId)|| StringUtils.isEmpty(sendUserId)||operType==null){
            return IJSONResult.errorMsg("参数错误");
        }

        //如果opentype没有对应的美剧类型 则直接抛出空错误信息
        if(StringUtils.isEmpty(OperatorFriendRequestTypeEnum.getMsgByType(operType))){
            return IJSONResult.errorMsg("类型错误");
        }
        if(operType == OperatorFriendRequestTypeEnum.IGNORE.getType()){
            friendsRequestService.delFriendRequest(sendUserId,acceptUserId);
            //删除好友的列表
        }else if(operType == OperatorFriendRequestTypeEnum.PASS.getType()){
           friendsRequestService.passFriendRequest(sendUserId,acceptUserId);
        }


        /*
        * 先清空 好友请求对应的东西
        * 然后0就忽略  1就把他加入到我的好友列表
        * */
        return IJSONResult.ok();
    }

    /*
    * 查询好友列表*/
    @PostMapping("/queryFriendList")
    public IJSONResult queryFriendList(String userId)  throws Exception {
        //判断传入参数不能为空
        if (StringUtils.isEmpty(userId)) {
            return IJSONResult.errorMsg("参数错误");
        }
        List<MyFriendsVO>list = myFriendsService.queryFriendList(userId);
        return  IJSONResult.ok(list);
    }


}
