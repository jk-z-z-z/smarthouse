package com.example.smarthouse.model.vo.room;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.model.entity.Room;
import lombok.Data;
import java.io.Serializable;

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

    private static final long serialVersionUID = 1L;

    public static Room voToObj(RoomVo roomVo) {
        if (roomVo == null) {
            return null;
        }
        Room room = new Room();
        BeanUtil.copyProperties(roomVo, room);
        return room;
    }

    public static RoomVo objToVo(Room room) {
        if (room == null) {
            return null;
        }
        RoomVo roomVo = new RoomVo();
        BeanUtil.copyProperties(room, roomVo);
        return roomVo;
    }
}
