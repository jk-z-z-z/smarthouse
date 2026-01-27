package com.example.smarthouse.model.dto.room;

import lombok.Data;
import java.io.Serializable;

@Data
public class RoomUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 房间名称
     */
    private String roomName;

    private static final long serialVersionUID = 1L;
}
