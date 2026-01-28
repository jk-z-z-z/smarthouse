package com.example.smarthouse.model.vo.furniture;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FurnitureVo implements Serializable {
    /**
     * id
     */
    private Long id;

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

    @Serial
    private static final long serialVersionUID = 1L;
}
