package org.example.web.rest.Controller;

import org.example.common.enums.CommonEnum;
import org.example.common.enums.UploadEnum;
import org.example.common.result.Result;
import org.example.utils.AliOssUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {

    @PostMapping("/upload")
    public Result<String> uploadFile(MultipartFile file) throws Exception{
        String originalFilename = file.getOriginalFilename();
        //保证文件的名字是唯一
        String fileName = UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
        String url = AliOssUtil.uploadFile(fileName,file.getInputStream());
        return Result.success(CommonEnum.SUCCESS.getCode(), UploadEnum.UPLOAD_SUCCESS.getMessage(), url);
    }
}
