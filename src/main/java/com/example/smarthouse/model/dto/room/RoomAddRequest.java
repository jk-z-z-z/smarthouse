package com.example.smarthouse.model.dto.room;

import lombok.Data;
import java.io.Serializable;

@Data
public class RoomAddRequest implements Serializable {

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 房子id
     */
    private Long houseId;

    private static final long serialVersionUID = 1L;
}
