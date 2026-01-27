package com.example.smarthouse.model.dto.room;

import com.example.smarthouse.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class RoomQueryRequest extends PageRequest implements Serializable {

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
}
