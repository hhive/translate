package org.example.translate.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.example.translate.commom.constant.CommonConstant;
import org.example.translate.commom.util.GsonUtil;
import org.example.translate.facade.request.UploadFileReqDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/6 20:01
 */
@Slf4j
@Service
public class TranslateCore {

    private Long pointer = 0L;

    private BufferedReader bufferedReader = null;

    /**
     * 执行
     */
    public String execute(UploadFileReqDto uploadFileReqDto) {

        /** 只支持英中互译 */
        String from = "en";
        String to = "zh-CHS";

        MultipartFile file = uploadFileReqDto.getFile();
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }

        String sourceFileName = file.getOriginalFilename();
        log.info(sourceFileName);
        /** 判断文件格式是否正确 */
        if (!sourceFileName.endsWith(".txt") && !sourceFileName.endsWith(".pdf")) {
            log.error("输入文件格式错误！");
            return null;
        }

        String txtFilePath = null;
        /** 如果收到的文件是txt格式的则跳过pdf解析 */
        if (sourceFileName.endsWith(".txt")) {
            txtFilePath = getFile(file, "source\\target\\").toString();
        } else {
            txtFilePath = pdfToTxt(file);
        }

        if (txtFilePath == null) {
            return "解析失败，请重新选择文件";
        }
        /** 判断是否已说明来源语言，否则就使用默认值 */
        if (uploadFileReqDto.getFrom()
            .equals(from) || uploadFileReqDto.getFrom()
            .equals(to)) {
            from = uploadFileReqDto.getFrom();
        }
        /** 判断是否已说明结果语言，否则就使用默认值 */
        if (uploadFileReqDto.getTo()
            .equals(from) || uploadFileReqDto.getTo()
            .equals(to)) {
            to = uploadFileReqDto.getTo();
        }

        // String fileName = this.getClass().getClassLoader().getResource("\\pdf\\finish\\369.pdf.txt").getPath();
        //  Path path = new WindowsPath();
        //String fileUtl = this.getClass().getResource("\\pdf\\finish\\369.pdf.txt").getFile();
        File txt = new File(txtFilePath);
        String resultFile = txt.getPath()
            .replace(txt.getName(), "") + "result" + File.separator + "result_" + txt.getName();
        log.info("翻译结果文件路径：{}", resultFile);
        String compareFile = txt.getPath()
            .replace(txt.getName(), "") + "compare" + File.separator + "compare_" + txt.getName();
        log.info("中英文对比文件路径：{}", compareFile);
        txtFilePath = txt.getName();
        Map<String, String> params = new HashMap<String, String>();
        while (this.pointer > -1) {
            params = convertParam(params, txt, from, to);
            /** 处理结果 */
            try {
                requestForHttp(resultFile, compareFile, CommonConstant.YOU_DAO_URL, params);
            } catch (IOException e) {
                log.error("翻译失败," + e);
                return CommonConstant.DOCUMENT_TRANSLATE_RESULT_FAILURE;
            }
        }
        log.info("文件翻译完毕");
        return CommonConstant.DOCUMENT_TRANSLATE_RESULT_SUCCESS;
    }

    private Map<String, String> convertParam(Map<String, String> params, File txt, String from, String to) {
        /** 组装参数 */
        if (params == null || params.size() == 0) {
            params.put("from", from);
            params.put("to", to);
            params.put("signType", "v3");
            params.put("appKey", CommonConstant.YOU_DAO_APP_KEY);

        }
        String q = parseTxt(txt);
        String salt = String.valueOf(System.currentTimeMillis());
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = CommonConstant.YOU_DAO_APP_KEY + truncate(q) + salt + curtime
            + CommonConstant.YOU_DAO_APP_SECRET;
        String sign = getDigest(signStr);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);

        return params;
    }

    /** 将pdf文献解析成txt */
    //怎么抛出错误给前台，考虑用aop
    public String pdfToTxt(MultipartFile sourceFile) {
        try {
            File pdf = new File(getFile(sourceFile, "source\\").toString());
            if (!pdf.isFile()) {
                log.error("路径%s文件为空！" + pdf);
                return null;
            }
            // sourceFile.transferTo(pdf);
            // 是否排序
            boolean sort = false;
            // 开始提取页数
            int startPage = 1;
            // 结束提取页数
            int endPage = Integer.MAX_VALUE;
            String content = null;
            PrintWriter writer = null;
            //输出txt文本路径
            // File pdf = new File(fileName);
            //File pdf = Objects.requireNonNull(file.listFiles())[0];
            String target = pdf.getPath()
                .replace(pdf.getName(), "") + "target" + File.separator + pdf.getName() + ".txt";
            log.info("解析pdf生成的txt文件路径：{}", target);
            PDDocument document = PDDocument.load(pdf);
            PDFTextStripper pts = new PDFTextStripper();
            endPage = document.getNumberOfPages();
            log.info("Total Page: " + endPage);
            pts.setStartPage(startPage);
            pts.setEndPage(endPage);
            try {
                //content就是从pdf中解析出来的文本
                content = pts.getText(document);
                pts.getResources();
                writer = new PrintWriter(new FileOutputStream(target));
                writer.write(content);// 写入文件内容
                writer.flush();
                writer.close();
            } catch (Exception e) {
                throw e;
            } finally {
                document.close();
            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /** MultipartFile转成File */
    private Path getFile(MultipartFile sourceFile, String targetDir) {
        Path pdfPath = null;
        try {
            File empty = new File("");
            //标准的路径 ;
            String courseFile = empty.getCanonicalPath();
            // String author = empty.getAbsolutePath();//绝对路径;
            String middlePath = ("\\biz-impl\\src\\main\\resources\\" + targetDir).replace("\\", File.separator);
            pdfPath = Paths.get(courseFile, middlePath, sourceFile.getOriginalFilename());
            try (OutputStream os = Files.newOutputStream(pdfPath)) {
                os.write(sourceFile.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pdfPath;
    }

    /**
     * 读取解析好的txt 有道翻译接口限制一次传送的字符数量，
     * 因此需要把文档分成一段段进行翻译，同时这样操作也有利于构建对比结果文档
     */
    private String parseTxt(File file) {
        if (!file.isFile()) {
            log.error("文件%s为空！" + file);
            return null;
        }
        // log.info("开始解析txt");
        StringBuilder content = new StringBuilder();
        String line = null;
        try {
            boolean flag = false;
            if (bufferedReader == null) {
                bufferedReader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            }
            // RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            while ((line = bufferedReader.readLine()) != null) {

                if (line.trim()
                    .endsWith("-")) {
                    line = line.replace("-", "");
                    content.append(" ")
                        .append(line);
                    flag = true;
                } else {
                    if (flag) {
                        content.append(line);
                        flag = false;
                    } else {
                        content.append(" ")
                            .append(line);
                    }
                }
                if (line.isEmpty() || line.trim()
                    .endsWith(".") || line.trim()
                    .endsWith("。")) {
                    break;
                }
            }
            if (line == null) {
                this.pointer = -1L;
            }
        } catch (IOException e) {
            log.error("开始解析txt失败" + e);
        }

        return content.toString()
            .replace("-\n", "")
            .replace("\n", " ");
    }

    public void requestForHttp(String result, String compare, String url, Map<String, String> params)
    throws IOException {

        /** 创建HttpClient */
        CloseableHttpClient httpClient = HttpClients.createDefault();

        /** httpPost */
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> paramsList = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, String>> it = params.entrySet()
            .iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> en = it.next();
            String key = en.getKey();
            String value = en.getValue();
            paramsList.add(new BasicNameValuePair(key, value));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, "UTF-8"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        try {
            Header[] contentType = httpResponse.getHeaders("Content-Type");
            log.info("Content-Type:" + contentType[0].getValue());
            if ("audio/mp3".equals(contentType[0].getValue())) {
                //如果响应是wav
                HttpEntity httpEntity = httpResponse.getEntity();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                httpResponse.getEntity()
                    .writeTo(baos);
                byte[] resultByte = baos.toByteArray();
                EntityUtils.consume(httpEntity);
                if (resultByte != null) {//合成成功
                    String file = "合成的音频存储路径" + System.currentTimeMillis() + ".mp3";
                    byte2File(resultByte, file);
                }
            } else {
                /** 响应不是音频流，直接显示结果 */
                HttpEntity httpEntity = httpResponse.getEntity();
                String json = EntityUtils.toString(httpEntity, StandardCharsets.UTF_8);
                EntityUtils.consume(httpEntity);
                HashMap hashMap = GsonUtil.json2Obj(json, HashMap.class);
                //log.info("hashMap is " + hashMap);
                String source = "原文：" + hashMap.get("query");
                log.info(source);
                String content = null;
                if (hashMap.get("translation") != null) {
                    content = hashMap.get("translation")
                        .toString()
                        .replace("[", "")
                        .replace("]", "");

                    log.info(content);
                    BufferedWriter out = null;
                    BufferedWriter out1 = null;
                    try {
                        /** 追加生成结果文件 */
                        out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(result), true),
                            StandardCharsets.UTF_8));
                        out.write("\n");
                        out.write("    ");
                        out.write(content);

                        /** 追加生成对比文件*/
                        out1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(compare), true),
                            StandardCharsets.UTF_8));
                        out1.write("\n");
                        out1.write("    ");
                        out1.write(source);
                        out1.write("\n");
                        out1.write("    " + "翻译：");
                        out1.write(content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            out.close();
                            out1.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } finally {
            try {
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                log.error("## release resouce error ##" + e);
            }
        }
    }

    /**
     * 生成解析字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * @param result 音频字节流
     * @param file 存储路径
     */
    private static void byte2File(byte[] result, String file) {
        File audioFile = new File(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(audioFile);
            fos.write(result);

        } catch (Exception e) {
            log.info(e.toString());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
