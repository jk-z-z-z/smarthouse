package com.example.smarthouse.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 图片处理工具类
 */
public class ImageUtils {

    /**
     * 根据坐标和宽高在图片上绘制矩形框并保存
     *
     * @param file     源文件
     * @param x        起始x坐标
     * @param y        起始y坐标
     * @param w        宽度
     * @param h        高度
     * @param destPath 目标保存路径
     */
    public static void drawRectAndSave(MultipartFile file, Long x, Long y, Long w, Long h, String destPath) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件不能为空");
        }
        if (x == null || y == null || w == null || h == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "框选参数不能为空");
        }

        try {
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片格式不支持");
            }

            // 获取 Graphics2D 对象用于绘图
            Graphics2D g2d = originalImage.createGraphics();

            // 设置画笔颜色为红色
            g2d.setColor(Color.RED);
            // 设置线条粗细为 3 像素
            g2d.setStroke(new BasicStroke(3.0f));

            int ix = x.intValue();
            int iy = y.intValue();
            int iw = w.intValue();
            int ih = h.intValue();

            // 绘制矩形框
            g2d.drawRect(ix, iy, iw, ih);

            // 释放资源
            g2d.dispose();

            File destFile = new File(destPath);
            // 确保父目录存在
            FileUtil.touch(destFile);

            String formatName = FileUtil.extName(destPath);
            if (StrUtil.isBlank(formatName)) {
                formatName = "jpg"; // 默认 jpg
            }

            if (!ImageIO.write(originalImage, formatName, destFile)) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片保存失败");
            }

        } catch (IOException e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图片处理异常: " + e.getMessage());
        }
    }
}
