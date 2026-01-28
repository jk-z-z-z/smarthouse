package com.example.smarthouse.model.dto.ai;

import lombok.Data;

@Data
public class AIAnalysisResult {
    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 家具ID
     */
    private Long furnitureId;
}
