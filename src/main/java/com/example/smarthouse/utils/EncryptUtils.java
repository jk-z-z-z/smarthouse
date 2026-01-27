package com.example.smarthouse.utils;

import org.springframework.util.DigestUtils;

public class EncryptUtils {
    public static String getEncryptPassword(String userPassword) {
        final String salt = "123456";
        return DigestUtils.md5DigestAsHex((salt+userPassword+salt).getBytes());
    }
}
