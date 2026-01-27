package com.example.smarthouse.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.mapper.HouseMapper;
import com.example.smarthouse.model.dto.house.HouseAddRequest;
import com.example.smarthouse.model.entity.House;
import com.example.smarthouse.model.entity.HouseUser;
import com.example.smarthouse.model.vo.user.LoginUserVo;
import com.example.smarthouse.service.HouseService;
import com.example.smarthouse.service.HouseUserService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        
        return house.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHouse(Long houseId) {
        if (houseId == null || houseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 删除关联关系
        QueryWrapper<HouseUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("house_id", houseId);
        houseUserService.remove(queryWrapper);
        // 删除房子
        return this.removeById(houseId);
    }
}
