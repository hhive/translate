package org.example.translate.biz;

import org.example.translate.facade.request.UploadFileReqDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/1 17:38
 */
public interface TranslatePdfBiz {

    /**
     * 翻译文档
     *
     * @param uploadFileReqDto
     */
    String execute(UploadFileReqDto uploadFileReqDto);

    /**
     * 将pdf文献解析成txt
     *
     * @param sourceFile
     * @return
     */
    String pdfToTxt(MultipartFile sourceFile);


}
