package com.example.smarthouse.app;

import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.example.smarthouse.exception.BusinessException;
import com.example.smarthouse.exception.ErrorCode;
import com.example.smarthouse.model.dto.ai.AIAnalysisResult;
import com.example.smarthouse.model.vo.furniture.FurnitureVo;
import com.example.smarthouse.model.vo.house.HouseVo;
import com.example.smarthouse.model.vo.room.RoomVo;
import com.example.smarthouse.service.HouseService;
import com.example.smarthouse.utils.ImageUtil;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class imageApp {

    @Resource
    private HouseService houseService;

    private final static String MODELNAME = "qwen-vl-plus";

    private final ChatModel chatModel;

    public imageApp(ChatModel dashscopeChatModel) {
        chatModel = dashscopeChatModel;
    }

    public String doChat(String promptText, String imagePath) {
        // 1. 创建媒体资源
        org.springframework.core.io.Resource imageResource = new ClassPathResource(imagePath);

        // 2. 创建Media对象
        String mediaType = ImageUtil.getMediaType(imagePath);
        Media imageMedia = new Media(MimeType.valueOf(mediaType), imageResource);

        // 3. 创建包含图像的消息
        UserMessage userMessage = UserMessage.builder()
                .text(promptText)
                .media(imageMedia)
                .build();
        // 4. 发送消息并获取响应
        ChatResponse response = chatModel.call(new Prompt(userMessage,
                DashScopeChatOptions.builder()
                        .model(MODELNAME)
                        .multiModel(true)
                        .build()));
        // 5. 获取并返回结果
        return response.getResult().getOutput().getText();
    }

    /**
     * 根据 HouseId 和目标图片进行分析
     * @param houseId 房子ID
     * @param targetFile 目标图片文件
     * @return AI分析结果
     */
    public AIAnalysisResult doChat(long houseId, MultipartFile targetFile) {
        // 1. 获取房子详情（包含房间和家具）
        HouseVo houseVo = houseService.getHouseDetail(houseId);
        if (houseVo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "未找到房子信息");
        }

        StringBuilder promptBuilder = new StringBuilder();
        promptBuilder.append("任务：视觉图像匹配。\n");
        promptBuilder.append("说明：我将提供一组【参考图片】，最后一张是【目标图片】。\n");
        promptBuilder.append("请严格按照以下步骤执行：\n");
        promptBuilder.append("1. 观察最后一张【目标图片】的视觉特征。\n");
        promptBuilder.append("2. 将其与前面的【参考图片】逐一进行视觉比对。\n");
        promptBuilder.append("3. 找出视觉上是同一个物体或最为相似的那张【参考图片】。\n");
        promptBuilder.append("4. 返回该匹配图片对应的 roomId 和 furnitureId。\n\n");
        promptBuilder.append("【参考图片列表】：\n");

        List<Media> mediaList = new ArrayList<>();
        int imageIndex = 1;
        String projectPath = System.getProperty("user.dir");

        // 2. 遍历房间和家具，构建 Prompt 和 Media
        List<RoomVo> roomList = houseVo.getRoomList();
        if (roomList == null || roomList.isEmpty()) {
             throw new BusinessException(ErrorCode.OPERATION_ERROR, "该房子下没有房间");
        }

        for (RoomVo room : roomList) {
            List<FurnitureVo> furnitureList = room.getFurnitureList();
            if (furnitureList != null && !furnitureList.isEmpty()) {
                for (FurnitureVo furniture : furnitureList) {
                    String fileName = furniture.getFileName();
                    if (StrUtil.isBlank(fileName)) {
                        continue;
                    }

                    // 构建文件路径: tmp/house_{houseId}/room_{roomID}/furniture_{furnitureId}/{fileName}
                    String filePath = String.format("%s/tmp/house_%d/room_%d/furniture_%d/%s",
                            projectPath, houseVo.getId(), room.getId(), furniture.getId(), fileName);
                        
                    File file = new File(filePath);
                    if (file.exists()) {
                        // 添加图片说明 (作为索引)
                        promptBuilder.append("[图片 #").append(imageIndex).append("] ");
                        promptBuilder.append("RoomID: ").append(room.getId());
                        promptBuilder.append(", FurnitureID: ").append(furniture.getId());
                        promptBuilder.append(", Name: ").append(furniture.getFurnitureName());
                        
                        // 添加更多元信息（如果有）
                        if (furniture.getWidth() != null && furniture.getHeight() != null) {
                            promptBuilder.append(", 尺寸: ").append(furniture.getWidth()).append("x").append(furniture.getHeight());
                        }
                        promptBuilder.append("\n");

                        // 添加 Media
                        try {
                            String mediaType = ImageUtil.getMediaType(fileName);
                            mediaList.add(new Media(MimeType.valueOf(mediaType), new FileSystemResource(file)));
                            imageIndex++;
                        } catch (Exception e) {
                            System.err.println("加载图片失败: " + filePath);
                        }
                    }
                }
            }
        }

        if (mediaList.isEmpty()) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该房子下没有任何家具图片可供比对");
        }

        // 3. 添加目标图片
        if (targetFile != null && !targetFile.isEmpty()) {
            promptBuilder.append("\n【目标图片】：\n");
            promptBuilder.append("图片 #").append(imageIndex).append(" (请分析这张图片，找出它与上述哪个图片编号匹配)\n");
            try {
                // 使用 InputStreamResource
                String contentType = targetFile.getContentType();
                if (contentType == null) {
                    contentType = "image/jpeg"; // 默认
                }
                mediaList.add(new Media(MimeType.valueOf(contentType), new InputStreamResource(targetFile.getInputStream())));
            } catch (IOException e) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "处理目标图片失败: " + e.getMessage());
            }
        } else {
             throw new BusinessException(ErrorCode.PARAMS_ERROR, "请上传目标图片");
        }

        // 4. 配置 BeanOutputConverter
        BeanOutputConverter<AIAnalysisResult> converter = new BeanOutputConverter<>(AIAnalysisResult.class);
        promptBuilder.append("\n请严格按照以下JSON格式返回结果，不要包含任何Markdown标记：\n");
        promptBuilder.append(converter.getFormat());

        // 5. 发送请求
        UserMessage userMessage = UserMessage.builder()
                .text(promptBuilder.toString())
                .media(mediaList.toArray(new Media[0]))
                .build();

        ChatResponse response = chatModel.call(new Prompt(userMessage,
                DashScopeChatOptions.builder()
                        .model(MODELNAME)
                        .multiModel(true)
                        .build()));

        String content = response.getResult().getOutput().getText();
        
        // 6. 转换结果
        try {
            assert content != null;
            return converter.convert(content);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI结果解析失败: " + content);
        }
    }
}
