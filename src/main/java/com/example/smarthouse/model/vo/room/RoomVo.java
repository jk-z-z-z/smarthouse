package com.example.smarthouse.model.vo.room;

import com.example.smarthouse.model.vo.furniture.FurnitureVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class RoomVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房子id
     */
    private Long houseId;

    /**
     * 家具列表
     */
    private List<FurnitureVo> furnitureList;

    @Serial
    private static final long serialVersionUID = 1L;
}
