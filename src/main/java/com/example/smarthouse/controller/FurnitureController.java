package com.example.smarthouse.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.DeleteRequest;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.model.dto.furniture.FurnitureAddRequest;
import com.example.smarthouse.model.dto.furniture.FurnitureQueryRequest;
import com.example.smarthouse.model.dto.furniture.FurnitureUpdateRequest;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.model.vo.furniture.FurnitureVo;
import com.example.smarthouse.service.FurnitureService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 家具接口
 */
@RestController
@RequestMapping("/furniture")
public class FurnitureController {

    @Resource
    private FurnitureService furnitureService;

    /**
     * 创建家具
     *
     * @param furnitureAddRequest
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addFurniture(@RequestBody FurnitureAddRequest furnitureAddRequest) {
        if (furnitureAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Furniture furniture = new Furniture();
        BeanUtil.copyProperties(furnitureAddRequest, furniture);
        boolean result = furnitureService.save(furniture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(furniture.getId());
    }

    /**
     * 删除家具
     *
     * @param deleteRequest
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFurniture(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = furnitureService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新家具
     *
     * @param furnitureUpdateRequest
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateFurniture(@RequestBody FurnitureUpdateRequest furnitureUpdateRequest) {
        if (furnitureUpdateRequest == null || furnitureUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Furniture furniture = new Furniture();
        BeanUtil.copyProperties(furnitureUpdateRequest, furniture);
        boolean result = furnitureService.updateById(furniture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取家具
     *
     * @param id
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<FurnitureVo> getFurnitureById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Furniture furniture = furnitureService.getById(id);
        ThrowUtils.throwIf(furniture == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(FurnitureVo.objToVo(furniture));
    }
}
