package com.example.smarthouse.model.vo.furniture;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.model.entity.Furniture;
import lombok.Data;
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

    private static final long serialVersionUID = 1L;

    public static Furniture voToObj(FurnitureVo furnitureVo) {
        if (furnitureVo == null) {
            return null;
        }
        Furniture furniture = new Furniture();
        BeanUtil.copyProperties(furnitureVo, furniture);
        return furniture;
    }

    public static FurnitureVo objToVo(Furniture furniture) {
        if (furniture == null) {
            return null;
        }
        FurnitureVo furnitureVo = new FurnitureVo();
        BeanUtil.copyProperties(furniture, furnitureVo);
        return furnitureVo;
    }
}
