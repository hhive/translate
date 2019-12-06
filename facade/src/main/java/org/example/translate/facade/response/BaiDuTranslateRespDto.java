package org.example.translate.facade.response;

import java.util.List;

import lombok.Data;

/**
 * @author lih@yunrong.cn
 * @version V2.1
 * @since 2.1.0 2019/12/6 11:50
 */
@Data
public class BaiDuTranslateRespDto {

    /**
     * 来源语言
     */
    private String from;

    /**
     * 结果语言
     */
    private String to;

    /**
     * 结果
     */
    private List<TranslateResult> trans_result;

    /**
     * 错误代码
     * 52000	成功
     * 52001	请求超时	重试
     * 52002	系统错误	重试
     * 52003	未授权用户	检查您的 appid 是否正确，或者服务是否开通
     * 54000	必填参数为空	检查是否少传参数
     * 54001	签名错误	请检查您的签名生成方法
     * 54003	访问频率受限	请降低您的调用频率
     * 54004	账户余额不足	请前往管理控制台为账户充值
     * 54005	长query请求频繁	请降低长query的发送频率，3s后再试
     * 58000	客户端IP非法	检查个人资料里填写的 IP地址 是否正确 可前往管理控制平台修改IP限制，IP可留空
     * 58001	译文语言方向不支持	检查译文语言是否在语言列表里
     * 58002	服务当前已关闭	请前往管理控制台开启服务
     * 90107	认证未通过或未生效	请前往我的认证查看认证进度
     */
    private String error_code;
}
