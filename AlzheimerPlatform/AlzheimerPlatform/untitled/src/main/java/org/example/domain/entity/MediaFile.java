package org.example.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 媒体文件实体类
 */
@Data
@TableName("media_file")
public class MediaFile {
    
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 文件名
     */
    @TableField("file_name")
    private String fileName;
    
    /**
     * 原始文件名
     */
    @TableField("original_file_name")
    private String originalFileName;
    
    /**
     * 文件类型（VIDEO/AUDIO）
     */
    @TableField("file_type")
    private String fileType;
    
    /**
     * 内容类型
     */
    @TableField("content_type")
    private String contentType;
    
    /**
     * 文件大小(字节)
     */
    @TableField("file_size")
    private Long fileSize;
    
    /**
     * 存储路径
     */
    @TableField("file_path")
    private String filePath;
    
    /**
     * 访问URL
     */
    @TableField("url")
    private String url;
    
    /**
     * 上传时间
     */
    @TableField("upload_time")
    private LocalDateTime uploadTime;
    
    /**
     * 逻辑删除标志(0-未删除，1-已删除)
     */
    @TableLogic
    @TableField("deleted")
    private Integer deleted;
} 