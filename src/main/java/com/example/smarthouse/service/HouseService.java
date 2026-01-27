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
}
