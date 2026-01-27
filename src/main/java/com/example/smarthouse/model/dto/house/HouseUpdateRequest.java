package com.example.smarthouse.model.dto.house;

import lombok.Data;
import java.io.Serializable;

@Data
public class HouseUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 房子名称
     */
    private String houseName;

    private static final long serialVersionUID = 1L;
}
