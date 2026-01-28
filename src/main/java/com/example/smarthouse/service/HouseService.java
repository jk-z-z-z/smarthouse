package com.example.smarthouse.service;

import com.example.smarthouse.model.entity.House;
import com.baomidou.mybatisplus.extension.service.IService;

import com.example.smarthouse.model.dto.house.HouseAddRequest;
import com.example.smarthouse.model.vo.user.LoginUserVo;

/**
* @author zzz
* @description 针对表【house(房子)】的数据库操作Service
* @createDate 2026-01-27 21:42:25
*/
public interface HouseService extends IService<House> {

    /**
     * 创建房子
     *
     * @param houseAddRequest
     * @param loginUser
     * @return
     */
    long addHouse(HouseAddRequest houseAddRequest, LoginUserVo loginUser);

    /**
     * 删除房子
     *
     * @param houseId
     * @return
     */
    boolean deleteHouse(Long houseId);

    /**
     * 根据用户 id 获取房子列表
     *
     * @param userId
     * @return
     */
    java.util.List<House> listHouseByUserId(long userId);

    /**
     * 获取房子详情（包含房间和家具）
     *
     * @param houseId
     * @return
     */
    com.example.smarthouse.model.vo.house.HouseVo getHouseDetail(long houseId);
}
