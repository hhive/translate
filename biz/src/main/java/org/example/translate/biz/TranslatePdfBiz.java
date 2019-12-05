package org.example.translate.biz;

import java.io.File;

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
     * @param fileName
     */
    void execute(String fileName);

    /**
     * 将pdf文献解析成txt
     *
     * @param sourceFile
     * @return
     */
    String pdfToTxt(MultipartFile sourceFile);


}
