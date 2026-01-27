package com.example.smarthouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.model.entity.HouseUser;
import com.example.smarthouse.service.HouseUserService;
import com.example.smarthouse.mapper.HouseUserMapper;
import org.springframework.stereotype.Service;

/**
* @author zzz
* @description 针对表【house_user】的数据库操作Service实现
* @createDate 2026-01-27 21:42:31
*/
@Service
public class HouseUserServiceImpl extends ServiceImpl<HouseUserMapper, HouseUser>
    implements HouseUserService{

}




