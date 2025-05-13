package org.example.web.rest.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.entity.MediaFile;
import org.example.domain.vo.MediaFileVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaFileService extends IService<MediaFile> {

    MediaFileVO uploadFile(MultipartFile file) throws Exception;


    List<MediaFileVO> getAllMediaFiles();

    MediaFileVO getMediaFileById(Long id);

    void deleteMediaFile(Long id);

    MediaFileVO convertToVO(MediaFile mediaFile);
}
