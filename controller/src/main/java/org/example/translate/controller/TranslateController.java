package org.example.translate.controller;

import java.io.File;

import org.example.translate.biz.TranslatePdfBiz;
import org.example.translate.facade.request.UploadFileReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/11/30 23:35
 */
@Slf4j
@RestController
@RequestMapping("/controller")
public class TranslateController {

    @Autowired
    private TranslatePdfBiz translatePdfBiz;

    @PostMapping("/uploadFile")
    public String upload(UploadFileReqDto uploadFileReqDto) {
        MultipartFile file = uploadFileReqDto.getFile();
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        log.info(file.getOriginalFilename());
        String target = translatePdfBiz.pdfToTxt(file);
        if (target != null) {
            translatePdfBiz.execute(target);
            return "成功";
        } else {
            return "解析失败，请重新选择文件";
        }
        // String filePath = "/Users/itinypocket/workspace/temp/";
        // File dest = new File(filePath + fileName);
        // try {
        //     file.transferTo(dest);
        //     log.info("上传成功");
        //     return "上传成功";
        // } catch (IOException e) {
        //     log.error(e.toString(), e);
        // }
        // return "上传失败！";
    }
}
