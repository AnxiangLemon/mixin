package com.lemon.mixin;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.lemon.mixin.entity.Users;
import com.lemon.mixin.mapper.FriendsRequestMapper;
import com.lemon.mixin.service.IFriendsRequestService;
import com.lemon.mixin.service.IUsersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MixinApplicationTests {

    @Autowired
    private IUsersService usersService;
    @Autowired
    private IFriendsRequestService iFriendsRequestService;
    @Autowired
    private FriendsRequestMapper requestMapper;

    @Test
    public void contextLoads() {

        System.out.println(usersService.queryUsernameInfo("lemon"));

       Users u= usersService.queryUserForLogin("lemon","123456");
       if (u!=null){
           System.out.println(u.toString());
       }

    }

    @Test
    public void testupdate(){
        Users user = new Users().setFaceImage("1asdfasdf").setId("181024109K5BKT7C").setNickname("测试数据");
        System.out.println(usersService.updateById(user));
    }

    /*测试获取用户信息*/
    @Test
    public  void testgetid(){
        Users user  =  usersService.getById("181024109K5BKT7C");
        System.out.println(user.toString());
    }

    /*测试二维码*/
    @Test
    public void testWCr() throws IOException, NotFoundException {
        String filePath = "C:\\Users\\AnxiangLemon\\Desktop\\temp\\支付宝.png";
        MultiFormatReader formatReader=new MultiFormatReader();
        File file=new File(filePath);
        BufferedImage image;

            image = ImageIO.read(file);
            BinaryBitmap binaryBitmap=new BinaryBitmap(new HybridBinarizer
                    (new BufferedImageLuminanceSource(image)));
            HashMap hints=new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET,"utf-8");    //指定字符编码为“utf-8”
            Result result=formatReader.decode(binaryBitmap,hints);
        System.out.println(result.toString());

    }

    /*测试获取好友请求*/
    @Test
    public  void testaddFriend(){
       List list =iFriendsRequestService.queryFriendsRequestVOList("181024109K5BKT7C");
       System.out.println(list.toString());
    }
}
