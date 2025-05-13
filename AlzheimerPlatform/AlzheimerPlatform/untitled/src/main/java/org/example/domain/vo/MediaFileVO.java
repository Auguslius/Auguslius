package org.example.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体文件VO类 - 用于返回给前端展示
 */
@Data
public class MediaFileVO {

    private Long id;
    private String fileName;     // 文件名
    private String originalFileName; // 原始文件名
    private String fileType;     // 文件类型（VIDEO/AUDIO）
    private Long fileSize;       // 文件大小(字节)
    private String url;          // 访问URL
    private String formattedSize; // 格式化后的文件大小（例如：2.5MB）
    private LocalDateTime uploadTime; // 上传时间
}