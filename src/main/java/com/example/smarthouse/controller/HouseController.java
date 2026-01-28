package com.example.smarthouse.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.DeleteRequest;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.constant.UserConstant;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.model.dto.house.HouseAddRequest;
import com.example.smarthouse.model.dto.house.HouseUpdateRequest;
import com.example.smarthouse.model.entity.House;
import com.example.smarthouse.model.vo.house.HouseVo;
import com.example.smarthouse.model.vo.user.LoginUserVo;
import com.example.smarthouse.service.HouseService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 房子接口
 */
@RestController
@RequestMapping("/house")
public class HouseController {

    @Resource
    private HouseService houseService;

    /**
     * 创建房子
     *
     */
    @PostMapping("/add")
    public BaseResponse<Long> addHouse(@RequestBody HouseAddRequest houseAddRequest, HttpServletRequest request) {
        if (houseAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        LoginUserVo loginUser = (LoginUserVo) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUser == null) {
             throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long newHouseId = houseService.addHouse(houseAddRequest, loginUser);
        return ResultUtils.success(newHouseId);
    }

    /**
     * 删除房子
     *
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteHouse(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = houseService.deleteHouse(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新房子
     *
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateHouse(@RequestBody HouseUpdateRequest houseUpdateRequest) {
        if (houseUpdateRequest == null || houseUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        House house = new House();
        house.setId(houseUpdateRequest.getId());
        house.setHouseName(houseUpdateRequest.getHouseName());
        boolean result = houseService.updateById(house);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取房子
     *
     */
    @GetMapping("/get")
    public BaseResponse<HouseVo> getHouseById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        House house = houseService.getById(id);
        ThrowUtils.throwIf(house == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(BeanUtil.copyProperties(house, HouseVo.class));
    }

    /**
     * 获取房子详情（包含房间和家具）
     *
     */
    @GetMapping("/get/detail")
    public BaseResponse<HouseVo> getHouseDetail(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        HouseVo houseVo = houseService.getHouseDetail(id);
        return ResultUtils.success(houseVo);
    }

    /**
     * 获取当前用户的所有房子
     *
     */
    @GetMapping("/my/list")
    public BaseResponse<List<HouseVo>> listMyHouses(HttpServletRequest request) {
        LoginUserVo loginUser = (LoginUserVo) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        long userId = loginUser.getId();
        List<House> houseList = houseService.listHouseByUserId(userId);
        List<HouseVo> houseVoList = houseList.stream()
                .map(house -> BeanUtil.copyProperties(house, HouseVo.class))
                .collect(Collectors.toList());
        return ResultUtils.success(houseVoList);
    }
}
