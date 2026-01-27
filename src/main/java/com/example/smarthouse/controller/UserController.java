package com.example.smarthouse.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.constant.UserConstant;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.exception.ThrowUtils;
import com.example.smarthouse.model.dto.user.UserAddRequest;
import com.example.smarthouse.model.dto.user.UserLoginRequest;
import com.example.smarthouse.model.entity.User;
import com.example.smarthouse.model.vo.user.LoginUserVo;
import com.example.smarthouse.service.UserService;
import com.example.smarthouse.utils.EncryptUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册接口
     *
     * @param userAddRequest 添加用户的请求参数，包含用户名、密码和确认密码
     * @return 返回注册成功的用户ID
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserAddRequest userAddRequest) {
        // 检查请求参数是否为空
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取用户名
        String username = userAddRequest.getUsername();
        // 创建查询条件，检查用户名是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        long count = userService.count(queryWrapper);
        // 如果用户名已存在则抛出异常
        ThrowUtils.throwIf(count > 0, ErrorCode.PARAMS_ERROR, "用户名已存在");
        // 获取用户密码和确认密码
        String userPassword = userAddRequest.getUserPassword();
        String checkPassword = userAddRequest.getCheckPassword();
        // 检查两次密码是否一致
        ThrowUtils.throwIf(!userPassword.equals(checkPassword), ErrorCode.PARAMS_ERROR, "两次密码不一致");
        // 对密码进行加密
        String password = EncryptUtils.getEncryptPassword(userPassword);
        // 创建新用户对象并设置用户名和加密后的密码
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        // 保存用户信息
        userService.save(user);
        return ResultUtils.success(user.getId());
    }

    @PostMapping("/login")
    public BaseResponse<LoginUserVo> login(@RequestBody UserLoginRequest userLoginRequest,
                                           HttpServletRequest request) {
        String username = userLoginRequest.getUsername();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasBlank(username, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        queryWrapper.eq("password", EncryptUtils.getEncryptPassword(userPassword));
        User user = userService.getOne(queryWrapper);
        ThrowUtils.throwIf(user == null, ErrorCode.PARAMS_ERROR, "用户名或密码错误");
        LoginUserVo loginUserVo = BeanUtil.copyProperties(user, LoginUserVo.class);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, loginUserVo);
        return ResultUtils.success(loginUserVo);
    }
}
