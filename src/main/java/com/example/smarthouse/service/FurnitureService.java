package com.example.smarthouse.service;

import com.example.smarthouse.model.entity.Furniture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.smarthouse.model.dto.furniture.FurnitureAddRequest;
import org.springframework.web.multipart.MultipartFile;

/**
* @author zzz
* @description 针对表【furniture(家具)】的数据库操作Service
* @createDate 2026-01-27 21:42:17
*/
public interface FurnitureService extends IService<Furniture> {

    /**
     * 添加家具（包含图片上传）
     *
     * @param furnitureAddRequest
     * @param file
     * @return
     */
    long addFurniture(FurnitureAddRequest furnitureAddRequest, MultipartFile file);
}
