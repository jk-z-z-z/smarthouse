package com.example.smarthouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.model.entity.User;
import com.example.smarthouse.service.UserService;
import com.example.smarthouse.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author zzz
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2026-01-27 21:42:39
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




