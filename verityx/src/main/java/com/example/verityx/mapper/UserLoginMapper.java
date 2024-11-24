package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.UserLogin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserLoginMapper extends BaseMapper<UserLogin> {

    // 修改为调用 XML 中的 SQL
    UserLogin findByUsername(String username);
}
