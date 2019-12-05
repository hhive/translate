package org.example.translate.facade.request;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/1 23:36
 */
@Data
public class UploadFileReqDto {
    private MultipartFile file;
}
