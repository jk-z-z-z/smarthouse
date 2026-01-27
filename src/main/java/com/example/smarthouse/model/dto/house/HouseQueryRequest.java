package com.example.smarthouse.model.dto.house;

import com.example.smarthouse.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class HouseQueryRequest extends PageRequest implements Serializable {

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
