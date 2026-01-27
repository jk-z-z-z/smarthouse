package com.example.smarthouse.model.vo.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class LoginUserVo implements Serializable {
    private long id;
    private String username;

    @Serial
    private final static long serialVersionUID = 1L;

}
