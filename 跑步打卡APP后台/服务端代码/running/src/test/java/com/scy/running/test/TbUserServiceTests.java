package com.scy.running.test;


import com.scy.running.model.TbUser;
import com.scy.running.service.ITbUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Scanner;


@SpringBootTest
public class TbUserServiceTests {

    @Autowired
    private ITbUserService userService;

    @Test
    void selectUserById() {


    }

    /**
     * 测试注册用户
     */
    @Test
    void insertUserByObj() {
        TbUser user = new TbUser();
        user.setUserNickName("Tom");
        user.setUserCellPhone("13327705432");
        user.setUserPassword("666555");
        userService.insertUserByObj(user);
    }

    /**
     * 测试用户登录
     */
    @Test
    void loginUserByObj(){
        String pass1 = "6665551";
        TbUser u = new TbUser();
        u.setUserPassword(pass1);
        u.setUserCellPhone("13327705432");
        Integer userId = userService.loginUserByObj(u);
        if(userId == null){
            System.out.println("登录失败，用户名不存在或密码不正确");
        }else{
            System.out.println("登录成功，欢迎您！");
        }

    }
}