package com.example.smarthouse.service.impl;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.model.entity.Room;
import com.example.smarthouse.service.RoomService;
import com.example.smarthouse.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.service.FurnitureService;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
* @author zzz
* @description 针对表【room(房间)】的数据库操作Service实现
* @createDate 2026-01-27 21:42:35
*/
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room>
    implements RoomService{

    @Resource
    private FurnitureService furnitureService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        Room room = this.getById(id);
        if (room == null) {
            return false;
        }

        // 1. 删除房间内的所有家具
        QueryWrapper<Furniture> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("room_id", id);
        List<Furniture> furnitureList = furnitureService.list(queryWrapper);
        if (furnitureList != null && !furnitureList.isEmpty()) {
            for (Furniture furniture : furnitureList) {
                // 调用 FurnitureService 的 removeById 以触发级联删除图片文件
                furnitureService.removeById(furniture.getId());
            }
        }

        // 2. 删除房间数据库记录
        boolean result = super.removeById(id);
        
        // 3. 删除房间目录
        if (result) {
            String projectPath = System.getProperty("user.dir");
            String roomDir = String.format("%s/tmp/house_%d/room_%d", projectPath, room.getHouseId(), room.getId());
            FileUtil.del(roomDir);
        }

        return result;
    }

    @Override
    public boolean save(Room entity) {
        boolean result = super.save(entity);
        if (result) {
            // 创建房间目录: tmp/house_{houseId}/room_{roomId}
            String projectPath = System.getProperty("user.dir");
            String roomDir = String.format("%s/tmp/house_%d/room_%d", projectPath, entity.getHouseId(), entity.getId());
            FileUtil.mkdir(roomDir);
        }
        return result;
    }
}




