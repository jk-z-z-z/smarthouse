package com.example.smarthouse.model.dto.house;

import lombok.Data;
import java.io.Serializable;

@Data
public class HouseAddRequest implements Serializable {

    /**
     * 房子名称
     */
    private String houseName;

    private static final long serialVersionUID = 1L;
}
