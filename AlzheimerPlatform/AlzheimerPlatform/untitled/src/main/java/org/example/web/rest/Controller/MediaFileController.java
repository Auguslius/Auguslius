package org.example.web.rest.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.enums.MediaFileEnum;
import org.example.common.result.Result;
import org.example.domain.vo.MediaFileVO;
import org.example.web.rest.service.MediaFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/media")
@CrossOrigin // 允许跨域访问
@RequiredArgsConstructor
public class MediaFileController {

    @Autowired
    private MediaFileService mediaFileService;

    /**
     * 上传媒体文件
     */
    @PostMapping("/upload")
    public Result<MediaFileVO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            MediaFileVO mediaFileVO = mediaFileService.uploadFile(file);
            return Result.success(MediaFileEnum.UPLOAD_SUCCESS.getCode(), MediaFileEnum.UPLOAD_SUCCESS.getMessage(), mediaFileVO);
        } catch (Exception e) {
            log.error(MediaFileEnum.UPLOAD_FAIL.getMessage(), e);
            return Result.fail(MediaFileEnum.UPLOAD_FAIL.getCode(), MediaFileEnum.UPLOAD_FAIL.getMessage());
        }
    }

    /**
     * 获取所有媒体文件
     */
    @GetMapping("/list")
    public Result<List<MediaFileVO>> getAllMediaFiles() {
        try {
            log.info("开始获取所有媒体文件列表");
            List<MediaFileVO> mediaFiles = mediaFileService.getAllMediaFiles();
            log.info("获取媒体文件列表成功，数量: {}", mediaFiles.size());
            return Result.success(MediaFileEnum.GET_FILE_LIST_SUCCESS.getCode(), MediaFileEnum.GET_FILE_LIST_SUCCESS.getMessage(),mediaFiles);
        } catch (Exception e) {
            log.error(MediaFileEnum.GET_FILE_LIST_FAIL.getMessage(), e);
            return Result.fail(MediaFileEnum.GET_FILE_LIST_FAIL.getCode(), MediaFileEnum.GET_FILE_LIST_FAIL.getMessage());
        }
    }

    /**
     * 根据ID获取媒体文件
     */
    @GetMapping("/{id}")
    public Result<MediaFileVO> getMediaFileById(@PathVariable Long id) {
        try {
            log.info("开始获取媒体文件，ID: {}", id);
            MediaFileVO mediaFile = mediaFileService.getMediaFileById(id);
            if (mediaFile != null) {
                log.info("获取媒体文件成功，ID: {}", id);
                return Result.success(MediaFileEnum.GET_FILE_SUCCESS.getCode(), MediaFileEnum.GET_FILE_SUCCESS.getMessage(), mediaFile);
            } else {
                log.warn(MediaFileEnum.GET_FILE_FAIL.getMessage(), id);
                return Result.fail(MediaFileEnum.GET_FILE_FAIL.getCode(), MediaFileEnum.GET_FILE_FAIL.getMessage());
            }
        } catch (Exception e) {
            log.error("获取媒体文件失败，ID: " + id, e);
            return Result.operationException(MediaFileEnum.GET_FILE_FAIL.getCode(), MediaFileEnum.GET_FILE_FAIL.getMessage());
        }
    }

    /**
     * 删除媒体文件
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMediaFile(@PathVariable Long id) {
        try {
            log.info("开始删除媒体文件，ID: {}", id);
            mediaFileService.deleteMediaFile(id);
            log.info("删除媒体文件成功，ID: {}", id);
            return Result.success(MediaFileEnum.DELETE_FILE_SUCCESS.getCode(), MediaFileEnum.DELETE_FILE_SUCCESS.getMessage(),null);
        } catch (Exception e) {
            log.error(MediaFileEnum.DELETE_FILE_FAIL.getMessage() + id, e);
            return Result.fail(MediaFileEnum.DELETE_FILE_FAIL.getCode(), MediaFileEnum.DELETE_FILE_FAIL.getMessage());
        }
    }

}
