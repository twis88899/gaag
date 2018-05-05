package com.twis.common.service;

public interface CaAuthService {
	/**
	 * 验证签名.
	 * @param certBase64 公钥证书信息
	 * @param signData 签名原文
	 * @param data 签名结果
	 * @return 1 有效签名 !=1 非法签名
	 * @throws Exception
	 */
	String szcaWSSignatureValidateString(String certBase64,String signData,String data) throws Exception;
	/**
	 * 验证证书.
	 * @param certBase64 公钥证书信息
	 * @return  证书有效 -1 证书无效，不是所信任的根 -2 证书无效，超过有效期 -3 证书无效，已加入黑名单
	 * @throws Exception
	 */
	String szcaWSCertificateValidateString(String certBase64) throws Exception;
	/**
	 * 返回证书结果.
	 * @param certBase64
	 * @return 证书主题,证书序列号,证书颁发者,证书有效起始期,证书有效结束期,用户身份证
	 * @throws Exception
	 */
	String szcaGetCertInforeturn(String certBase64) throws Exception;
	
	String szcaWSSignatureValidatePkcs7String(String signResult) throws Exception;
}
