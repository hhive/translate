package org.example.translate.controller;

import org.example.translate.biz.TranslatePdfBiz;
import org.example.translate.facade.request.UploadFileReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/11/30 23:35
 */
@Slf4j
@RestController
@RequestMapping("/translateController")
public class TranslateController {

    @Autowired
    private TranslatePdfBiz translatePdfBiz;

    @PostMapping("/uploadFile")
    public String upload(UploadFileReqDto uploadFileReqDto) {
        log.info("TranslateController.upload入参：{}", uploadFileReqDto);
        return translatePdfBiz.execute(uploadFileReqDto);
    }
}
