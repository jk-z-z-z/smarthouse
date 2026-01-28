package com.example.smarthouse.controller;

import com.example.smarthouse.app.imageApp;
import com.example.smarthouse.common.BaseResponse;
import com.example.smarthouse.common.ResultUtils;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.model.dto.ai.AIAnalysisResult;
import jakarta.annotation.Resource;import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ai")
public class AIController {

    @Resource
    private imageApp imageApp;

    /**
     * 分析上传的图片，判断其属于哪个房间
     * @param houseId 房子ID
     * @param file 目标图片
     * @return 分析结果
     */
    @PostMapping("/analyze")
    public BaseResponse<AIAnalysisResult> analyzeFurniture(
            @RequestParam("houseId") Long houseId,
            @RequestParam("file") MultipartFile file) {
        
        if (houseId == null || houseId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "House ID无效");
        }
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传图片");
        }

        try {
            // 调用 imageApp 的 doChat 方法进行分析
            AIAnalysisResult result = imageApp.doChat(houseId, file);
            return ResultUtils.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI分析失败: " + e.getMessage());
        }
    }
}
