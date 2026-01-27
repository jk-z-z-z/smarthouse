package com.example.smarthouse.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 房子
 * @TableName house
 */
@TableName(value ="house")
@Data
public class House implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 房子名称
     */
    private String houseName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}