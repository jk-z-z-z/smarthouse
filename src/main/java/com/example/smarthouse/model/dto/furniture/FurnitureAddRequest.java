package com.example.smarthouse.model.dto.furniture;

import lombok.Data;
import java.io.Serializable;

@Data
public class FurnitureAddRequest implements Serializable {

    /**
     * 家具名称
     */
    private String furnitureName;

    /**
     * x坐标
     */
    private Long positionX;

    /**
     * y坐标
     */
    private Long positionY;

    /**
     * 宽度
     */
    private Long width;

    /**
     * 高度
     */
    private Long height;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 图片名称
     */
    private String fileName;

    private static final long serialVersionUID = 1L;
}
