package com.example.smarthouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.mapper.FurnitureMapper;
import com.example.smarthouse.model.dto.furniture.FurnitureAddRequest;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.model.entity.Room;
import com.example.smarthouse.service.FurnitureService;
import com.example.smarthouse.service.RoomService;
import com.example.smarthouse.utils.ImageUtils;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * @author zzz
 * @description 针对表【furniture(家具)】的数据库操作Service实现
 * @createDate 2026-01-27 21:42:17
 */
@Service
public class FurnitureServiceImpl extends ServiceImpl<FurnitureMapper, Furniture>
    implements FurnitureService{

    @Resource
    @Lazy
    private RoomService roomService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Furniture furniture = this.getById(id);
        if (furniture == null) {
            return false;
        }

        // 删除数据库记录
        boolean result = super.removeById(id);
        if (!result) {
            return false;
        }

        // 删除文件
        // 无论是否有文件名，都尝试删除目录，因为目录结构是固定的
        Room room = roomService.getById(furniture.getRoomId());
        if (room != null) {
            String projectPath = System.getProperty("user.dir");
            // 删除整个家具目录
            String furnitureDir = String.format("%s/tmp/house_%d/room_%d/furniture_%d", 
                    projectPath, room.getHouseId(), room.getId(), furniture.getId());
            FileUtil.del(furnitureDir);
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addFurniture(FurnitureAddRequest furnitureAddRequest, MultipartFile file) {
        if (furnitureAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long roomId = furnitureAddRequest.getRoomId();
        if (roomId == null || roomId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "房间ID不能为空");
        }
        Room room = roomService.getById(roomId);
        if (room == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "房间不存在");
        }
        Long houseId = room.getHouseId();

        Furniture furniture = new Furniture();
        BeanUtil.copyProperties(furnitureAddRequest, furniture);
        furniture.setFileName(file.getOriginalFilename());
        // 保存到数据库，获取ID
        boolean result = this.save(furniture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        
        Long furnitureId = furniture.getId();

        // 处理文件上传
        if (file != null && !file.isEmpty()) {
            String originalFilename = file.getOriginalFilename();
            // 路径格式: tmp/house_{houseId}/room_{roomID}/furniture_{furnitureId}/fileName
            // 使用绝对路径避免相对路径问题
            String projectPath = System.getProperty("user.dir");
            String parentDir = String.format("%s/tmp/house_%d/room_%d/furniture_%d", projectPath, houseId, roomId, furnitureId);
            String filePath = parentDir + File.separator + originalFilename;
            
            // 确保父目录存在
            FileUtil.mkdir(parentDir);
            
            try {
                // 判断是否需要框选
                if (furnitureAddRequest.getPositionX() != null
                        && furnitureAddRequest.getPositionY() != null
                        && furnitureAddRequest.getWidth() != null
                        && furnitureAddRequest.getHeight() != null) {

                    ImageUtils.drawRectAndSave(file,
                            furnitureAddRequest.getPositionX(),
                            furnitureAddRequest.getPositionY(),
                            furnitureAddRequest.getWidth(),
                            furnitureAddRequest.getHeight(),
                            filePath);
                } else {
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败: " + e.getMessage());
            }
        }

        return furnitureId;
    }
}
