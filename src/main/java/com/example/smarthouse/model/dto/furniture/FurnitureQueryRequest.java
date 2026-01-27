package com.example.smarthouse.model.dto.furniture;

import com.example.smarthouse.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class FurnitureQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 家具名称
     */
    private String furnitureName;

    /**
     * 房间id
     */
    private Long roomId;

    private static final long serialVersionUID = 1L;
}
