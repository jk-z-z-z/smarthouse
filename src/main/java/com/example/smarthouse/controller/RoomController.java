package com.example.smarthouse.controller;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.DeleteRequest;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.model.dto.room.RoomAddRequest;
import com.example.smarthouse.model.dto.room.RoomUpdateRequest;
import com.example.smarthouse.model.entity.Room;
import com.example.smarthouse.model.vo.room.RoomVo;
import com.example.smarthouse.service.RoomService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;


/**
 * 房间接口
 */
@RestController
@RequestMapping("/room")
public class RoomController {

    @Resource
    private RoomService roomService;

    /**
     * 创建房间
     *
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRoom(@RequestBody RoomAddRequest roomAddRequest) {
        if (roomAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = new Room();
        BeanUtil.copyProperties(roomAddRequest, room);
        boolean result = roomService.save(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(room.getId());
    }

    /**
     * 删除房间
     *
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRoom(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = roomService.removeById(deleteRequest.getId());
        return ResultUtils.success(result);
    }

    /**
     * 更新房间
     *
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateRoom(@RequestBody RoomUpdateRequest roomUpdateRequest) {
        if (roomUpdateRequest == null || roomUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = new Room();
        BeanUtil.copyProperties(roomUpdateRequest, room);
        boolean result = roomService.updateById(room);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取房间
     *
     */
    @GetMapping("/get")
    public BaseResponse<RoomVo> getRoomById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Room room = roomService.getById(id);
        ThrowUtils.throwIf(room == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(BeanUtil.copyProperties(room, RoomVo.class));
    }
}
