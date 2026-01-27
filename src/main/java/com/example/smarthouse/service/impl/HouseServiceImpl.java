package com.example.smarthouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.model.entity.House;
import com.example.smarthouse.service.HouseService;
import com.example.smarthouse.mapper.HouseMapper;
import org.springframework.stereotype.Service;

/**
* @author zzz
* @description 针对表【house(房子)】的数据库操作Service实现
* @createDate 2026-01-27 21:42:25
*/
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House>
    implements HouseService{

}




