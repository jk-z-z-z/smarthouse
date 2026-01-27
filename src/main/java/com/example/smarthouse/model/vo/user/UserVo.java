package com.example.smarthouse.model.vo.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserVo implements Serializable {
    private String username;

    @Serial
    private final static long serialVersionUID = 1L;

}
