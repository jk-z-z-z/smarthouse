package com.example.smarthouse.model.vo.house;

import com.example.smarthouse.model.vo.room.RoomVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class HouseVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 房子名称
     */
    private String houseName;

    /**
     * 房间列表
     */
    private List<RoomVo> roomList;

    @Serial
    private static final long serialVersionUID = 1L;
}
