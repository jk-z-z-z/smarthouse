package com.example.smarthouse.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 家具
 * @TableName furniture
 */
@TableName(value ="furniture")
@Data
public class Furniture implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
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
     * 
     */
    private Long roomId;

    /**
     * 图片名称
     */
    private String fileName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}