package com.example.smarthouse.model.vo.house;

import cn.hutool.core.bean.BeanUtil;
import com.example.smarthouse.model.entity.House;
import lombok.Data;
import java.io.Serializable;

@Data
public class HouseVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 房子名称
     */
    private String houseName;

    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param houseVo
     * @return
     */
    public static House voToObj(HouseVo houseVo) {
        if (houseVo == null) {
            return null;
        }
        House house = new House();
        BeanUtil.copyProperties(houseVo, house);
        return house;
    }

    /**
     * 对象转包装类
     *
     * @param house
     * @return
     */
    public static HouseVo objToVo(House house) {
        if (house == null) {
            return null;
        }
        HouseVo houseVo = new HouseVo();
        BeanUtil.copyProperties(house, houseVo);
        return houseVo;
    }
}
