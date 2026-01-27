package com.example.smarthouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.service.FurnitureService;
import com.example.smarthouse.mapper.FurnitureMapper;
import org.springframework.stereotype.Service;

/**
* @author zzz
* @description 针对表【furniture(家具)】的数据库操作Service实现
* @createDate 2026-01-27 21:42:17
*/
@Service
public class FurnitureServiceImpl extends ServiceImpl<FurnitureMapper, Furniture>
    implements FurnitureService{

}




