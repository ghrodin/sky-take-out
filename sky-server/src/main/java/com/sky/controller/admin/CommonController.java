package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.MinioUtil;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Api("通用接口")
@Slf4j
public class CommonController {
    @Autowired
    private MinioUtil minioUtil;

    /**
     * 文件上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传{}", file);
        try {
            //原始文件名
            String originalFilename = file.getOriginalFilename();
            //原始文件扩展名
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //构造新文件名
            String objectName = UUID.randomUUID() + extension;

            String uploadPath=minioUtil.upload(file.getBytes(), objectName);
            return Result.success(uploadPath);
        } catch (IOException e) {
            log.info(MessageConstant.UPLOAD_FAILED+":{}",e);
            e.printStackTrace();
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
