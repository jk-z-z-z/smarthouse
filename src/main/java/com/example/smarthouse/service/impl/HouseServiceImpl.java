package com.example.smarthouse.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.mapper.HouseMapper;
import com.example.smarthouse.model.dto.house.HouseAddRequest;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.model.entity.House;
import com.example.smarthouse.model.entity.HouseUser;
import com.example.smarthouse.model.entity.Room;
import com.example.smarthouse.model.vo.furniture.FurnitureVo;
import com.example.smarthouse.model.vo.house.HouseVo;
import com.example.smarthouse.model.vo.room.RoomVo;
import com.example.smarthouse.model.vo.user.LoginUserVo;
import com.example.smarthouse.service.FurnitureService;
import com.example.smarthouse.service.HouseService;
import com.example.smarthouse.service.HouseUserService;
import com.example.smarthouse.service.RoomService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.Serializable;

/**
* @author zzz
* @description 针对表【house(房子)】的数据库操作Service实现
* @createDate 2026-01-27 21:42:25
*/
@Service
public class HouseServiceImpl extends ServiceImpl<HouseMapper, House>
    implements HouseService{

    @Resource
    private HouseUserService houseUserService;

    @Resource
    private RoomService roomService;

    @Resource
    private FurnitureService furnitureService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Serializable id) {
        // 1. 获取房子信息
        House house = this.getById(id);
        if (house == null) {
            return false;
        }

        // 2. 删除关联的用户关系
        QueryWrapper<HouseUser> houseUserQueryWrapper = new QueryWrapper<>();
        houseUserQueryWrapper.eq("house_id", id);
        houseUserService.remove(houseUserQueryWrapper);

        // 3. 删除房子内的所有房间
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("house_id", id);
        List<Room> roomList = roomService.list(roomQueryWrapper);
        if (roomList != null && !roomList.isEmpty()) {
            for (Room room : roomList) {
                // 调用 RoomService 的 removeById 以触发级联删除家具
                roomService.removeById(room.getId());
            }
        }

        // 4. 删除房子数据库记录
        boolean result = super.removeById(id);

        // 5. 删除房子目录
        if (result) {
            String projectPath = System.getProperty("user.dir");
            String houseDir = String.format("%s/tmp/house_%d", projectPath, id);
            FileUtil.del(houseDir);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public long addHouse(HouseAddRequest houseAddRequest, LoginUserVo loginUser) {
        ThrowUtils.throwIf(houseAddRequest == null, ErrorCode.PARAMS_ERROR);
        String houseName = houseAddRequest.getHouseName();
        ThrowUtils.throwIf(StrUtil.isBlank(houseName), ErrorCode.PARAMS_ERROR);
        
        // 保存房子
        House house = new House();
        house.setHouseName(houseName);
        boolean result = this.save(house);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        
        // 保存关联关系
        HouseUser houseUser = new HouseUser();
        houseUser.setHouseId(house.getId());
        houseUser.setUserId(loginUser.getId());
        boolean userResult = houseUserService.save(houseUser);
        ThrowUtils.throwIf(!userResult, ErrorCode.OPERATION_ERROR);
        
        // 创建房子目录: tmp/house_{houseId}
        String projectPath = System.getProperty("user.dir");
        String houseDir = String.format("%s/tmp/house_%d", projectPath, house.getId());
        FileUtil.mkdir(houseDir);

        return house.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHouse(Long houseId) {
        if (houseId == null || houseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 调用重写的 removeById 以触发级联删除
        return this.removeById(houseId);
    }

    @Override
    public List<House> listHouseByUserId(long userId) {
        if (userId <= 0) {
            return List.of();
        }
        // 查询关联表
        QueryWrapper<HouseUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<HouseUser> houseUserList = houseUserService.list(queryWrapper);
        if (houseUserList == null || houseUserList.isEmpty()) {
            return List.of();
        }
        // 获取房子 id 集合
        List<Long> houseIds = houseUserList.stream()
                .map(HouseUser::getHouseId)
                .collect(Collectors.toList());
        // 查询房子信息
        return this.listByIds(houseIds);
    }

    @Override
    public HouseVo getHouseDetail(long houseId) {
        if (houseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 1. 获取房子信息
        House house = this.getById(houseId);
        ThrowUtils.throwIf(house == null, ErrorCode.NOT_FOUND_ERROR);
        HouseVo houseVo = BeanUtil.copyProperties(house, HouseVo.class);

        // 2. 获取该房子下的所有房间
        QueryWrapper<Room> roomQueryWrapper = new QueryWrapper<>();
        roomQueryWrapper.eq("house_id", houseId);
        List<Room> roomList = roomService.list(roomQueryWrapper);
        
        if (roomList != null && !roomList.isEmpty()) {
            List<Long> roomIds = roomList.stream().map(Room::getId).collect(Collectors.toList());
            
            // 3. 获取所有房间下的家具
            QueryWrapper<Furniture> furnitureQueryWrapper = new QueryWrapper<>();
            furnitureQueryWrapper.in("room_id", roomIds);
            List<Furniture> furnitureList = furnitureService.list(furnitureQueryWrapper);

            // 4. 按 roomId 分组家具
            Map<Long, List<Furniture>> furnitureMap = furnitureList.stream()
                    .collect(Collectors.groupingBy(Furniture::getRoomId));

            // 5. 组装 RoomVo
            List<RoomVo> roomVoList = roomList.stream().map(room -> {
                RoomVo roomVo = BeanUtil.copyProperties(room, RoomVo.class);
                List<Furniture> roomFurnitures = furnitureMap.getOrDefault(room.getId(), List.of());
                List<FurnitureVo> furnitureVoList = roomFurnitures.stream()
                        .map(furniture -> BeanUtil.copyProperties(furniture, FurnitureVo.class))
                        .collect(Collectors.toList());
                roomVo.setFurnitureList(furnitureVoList);
                return roomVo;
            }).collect(Collectors.toList());

            houseVo.setRoomList(roomVoList);
        } else {
            houseVo.setRoomList(List.of());
        }

        return houseVo;
    }
}
