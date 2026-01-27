package com.example.smarthouse.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserAddRequest implements Serializable {
    private String username;
    private String userPassword;
    private String checkPassword;
    @Serial
    private static final long serialVersionUID = 1L;
}
