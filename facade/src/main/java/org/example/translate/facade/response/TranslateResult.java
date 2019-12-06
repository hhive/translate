package org.example.translate.facade.response;

import lombok.Data;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/6 11:35
 */
@Data
public class TranslateResult {
    /** 原文 */
    private String src;
    /** 结果 */
    private String dst;
}
