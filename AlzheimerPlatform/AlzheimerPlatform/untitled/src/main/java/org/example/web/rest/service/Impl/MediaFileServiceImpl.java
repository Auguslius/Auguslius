package org.example.web.rest.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.common.enums.MediaFileEnum;
import org.example.exception.BusinessException;
import org.example.domain.entity.MediaFile;
import org.example.domain.vo.MediaFileVO;
import org.example.utils.AliOssUtil;
import org.example.web.rest.mapper.MediaFileMapper;
import org.example.web.rest.service.MediaFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFile> implements MediaFileService {

    private static final String AUDIO_TYPE = "AUDIO";
    private static final String VIDEO_TYPE = "VIDEO";

    private static final String IMAGE_TYPE = "IMAGE";

    @Override
    public MediaFileVO uploadFile(MultipartFile file) throws Exception {
        if (file.isEmpty()){
            throw new IllegalArgumentException( MediaFileEnum.EMPTY_FILE.getMessage());
        }
        log.info("开始处理文件上传：{}", file.getOriginalFilename());

        // 获取原始文件名和后缀
        String originalFileName = file.getOriginalFilename();
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."));

        // 生成新的文件名
        String newFileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        //确定文件类型
        String contentType = file.getContentType();
        String fileType;

        if (contentType != null && contentType.startsWith("audio")) {
            fileType = AUDIO_TYPE;
        } else if (contentType != null && contentType.startsWith("video")) {
            fileType = VIDEO_TYPE;
        } else if (contentType != null && contentType.startsWith("image")) {
            fileType = IMAGE_TYPE;
        } else {
            log.warn(MediaFileEnum.EMPTY_FILE_TYPE.getMessage(), contentType);
            throw new IllegalArgumentException( MediaFileEnum.EMPTY_FILE_TYPE.getMessage());
        }
        log.info("文件类型: {}, 内容类型: {}", fileType, contentType);

        // 生成OSS路径，按日期和类型分类存储
        String dateDir = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String objectName = "media/" + fileType.toLowerCase() + "/" + dateDir + "/" + newFileName;

        log.info("开始上传文件到阿里云OSS，对象名称: {}", objectName);

        // 上传到阿里云OSS
        String url = AliOssUtil.uploadFile(objectName, file.getInputStream());

        log.info("文件上传到阿里云OSS成功，访问URL: {}", url);

        // 创建MediaFile对象
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileName(newFileName);
        mediaFile.setOriginalFileName(originalFileName);
        mediaFile.setFileType(fileType);
        mediaFile.setContentType(contentType);
        mediaFile.setFileSize(file.getSize());
        mediaFile.setFilePath(objectName);
        mediaFile.setUrl(url);
        mediaFile.setUploadTime(LocalDateTime.now());

        log.info("开始保存媒体文件信息到数据库");
        // 保存到数据库
        try {
            save(mediaFile);
            log.info("媒体文件信息保存到数据库成功，ID: {}", mediaFile.getId());
        } catch (Exception e) {
            log.error(MediaFileEnum.UPLOAD_FAIL.getMessage(), e);
            throw new BusinessException(MediaFileEnum.UPLOAD_FAIL.getCode(), MediaFileEnum.UPLOAD_FAIL.getMessage()+e.getMessage());
        }

        // 转换为VO并返回
        return convertToVO(mediaFile);
    }

    @Override
    public List<MediaFileVO> getAllMediaFiles() {
        try {
            log.info("开始查询所有媒体文件");
            List<MediaFile> mediaFiles = list();
            log.info("查询到 {} 个媒体文件", mediaFiles.size());
            return mediaFiles.stream().map(this::convertToVO).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(MediaFileEnum.GET_FILE_FAIL.getMessage()+e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public MediaFileVO getMediaFileById(Long id) {
        try {
            log.info("开始根据ID查询媒体文件: {}", id);
            MediaFile mediaFile = getById(id);
            if (mediaFile == null) {
                log.warn("未找到ID为 {} 的媒体文件", id);
                return null;
            }
            log.info("成功查询到ID为 {} 的媒体文件", id);
            return convertToVO(mediaFile);
        } catch (Exception e) {
            log.error(MediaFileEnum.GET_FILE_FAIL.getMessage()+e.getMessage());
            return null;
        }
    }

    @Override
    public void deleteMediaFile(Long id) {
        try {
            log.info("开始删除ID为 {} 的媒体文件", id);
            boolean result = removeById(id);
            if (result) {
                log.info("成功删除ID为 {} 的媒体文件", id);
            } else {
                log.warn("删除ID为 {} 的媒体文件失败，可能文件不存在", id);
            }
        } catch (Exception e) {
            log.error("删除ID为 " + id + " 的媒体文件时发生异常", e);
            throw new BusinessException(MediaFileEnum.DELETE_FILE_FAIL.getCode(), MediaFileEnum.DELETE_FILE_FAIL.getMessage()+e.getMessage());
        }
    }

    @Override
    public MediaFileVO convertToVO(MediaFile mediaFile) {
        if (mediaFile == null) {
            return null;
        }

        MediaFileVO vo = new MediaFileVO();
        vo.setId(mediaFile.getId());
        vo.setFileName(mediaFile.getFileName());
        vo.setOriginalFileName(mediaFile.getOriginalFileName());
        vo.setFileType(mediaFile.getFileType());
        vo.setFileSize(mediaFile.getFileSize());
        vo.setUrl(mediaFile.getUrl());
        vo.setUploadTime(mediaFile.getUploadTime());

        // 格式化文件大小
        vo.setFormattedSize(formatFileSize(mediaFile.getFileSize()));

        return vo;
    }

    /**
     * 格式化文件大小
     */
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return String.format("%.2f", size / 1024.0) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.2f", size / (1024.0 * 1024)) + "MB";
        } else {
            return String.format("%.2f", size / (1024.0 * 1024 * 1024)) + "GB";
        }
    }
}