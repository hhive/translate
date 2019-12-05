package org.example.translate.facade.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/1 23:36
 */
@Data
public class UploadFileReqDto {

    /**
     * 待翻译的文件
     */
    private MultipartFile file;

    /**
     * 文档初始语言
     */
    private String from;

    /**
     * 文档结果语言
     */
    private String to;
}
