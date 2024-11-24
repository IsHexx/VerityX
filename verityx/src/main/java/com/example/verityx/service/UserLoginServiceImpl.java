package com.example.verityx.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.verityx.entity.UserLogin;
import com.example.verityx.mapper.UserLoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Autowired
    private UserLoginMapper userLoginMapper;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserLogin login(String username, String password) {
        System.out.println("====== 开始登录流程 ======");
        System.out.println("尝试登录用户: " + username);

        // 方法1：使用 MyBatis-Plus 的 LambdaQueryWrapper
        LambdaQueryWrapper<UserLogin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserLogin::getUsername, username);
        System.out.println("执行的SQL语句将会是: SELECT * FROM user_login WHERE username = '" + username + "'");
//        UserLogin user = userLoginMapper.selectOne(queryWrapper);
        UserLogin user = userLoginMapper.findByUsername(username);
        // 如果上面的方法没有找到用户，尝试使用自定义的查询方法
//        if (user == null) {
//            System.out.println("尝试使用自定义查询方法");
//            user = userLoginMapper.findByUsername(username);
//        }

        // 打印查询结果
        if (user != null) {
            System.out.println("找到用户: " + user.getUsername());
            System.out.println("用户ID: " + user.getId());
            System.out.println("账号状态: " + (user.getIsActive() ? "激活" : "未激活"));

            // 验证密码
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("密码验证结果: " + (passwordMatch ? "正确" : "错误"));

            if (passwordMatch && user.getIsActive()) {
                System.out.println("登录成功");
                user.setLastLoginAt(java.time.LocalDateTime.now());
                userLoginMapper.updateById(user);
                return user;
            }
        } else {
            System.out.println("未找到用户: " + username);
        }

        System.out.println("====== 登录失败 ======");
        return null;
    }
}