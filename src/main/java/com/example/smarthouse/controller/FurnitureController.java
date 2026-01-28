package com.example.smarthouse.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.DeleteRequest;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.model.dto.furniture.FurnitureAddRequest;
import com.example.smarthouse.model.dto.furniture.FurnitureUpdateRequest;
import com.example.smarthouse.model.entity.Furniture;
import com.example.smarthouse.model.vo.furniture.FurnitureVo;
import com.example.smarthouse.service.FurnitureService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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
     */
    @PostMapping("/add")
    public BaseResponse<Long> addFurniture(FurnitureAddRequest furnitureAddRequest,
                                           @RequestPart(value = "file", required = false) MultipartFile file) {
        if (furnitureAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long furnitureId = furnitureService.addFurniture(furnitureAddRequest, file);
        return ResultUtils.success(furnitureId);
    }

    /**
     * 删除家具
     *
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
     */
    @GetMapping("/get")
    public BaseResponse<FurnitureVo> getFurnitureById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Furniture furniture = furnitureService.getById(id);
        ThrowUtils.throwIf(furniture == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(BeanUtil.copyProperties(furniture, FurnitureVo.class));
    }
}
