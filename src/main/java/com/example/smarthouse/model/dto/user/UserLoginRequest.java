package com.example.smarthouse.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private String username;
    private String userPassword;

    @Serial
    private final static long serialVersionUID = 1L;
}
