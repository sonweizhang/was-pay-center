package com.pay.common.sign;

import java.io.UnsupportedEncodingException;
import java.security.SignatureException;

import org.apache.commons.codec.digest.DigestUtils;

public class WkSign {
	//请求用key
	public static String key = "nq1186l1zf3n45zm84rn1qwm93cw0mqg";
	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";
	// 签名方式 不需修改
	public static String sign_type = "MD5";

	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @return 签名结果
	 */
	public static String sign(String text) {
		text = text + key;
		return DigestUtils.md5Hex(getContentBytes(text, input_charset));
	}

	/**
	 * 签名字符串
	 * @param text 需要签名的字符串
	 * @param sign 签名结果
	 * @return 签名结果
	 */
	public static boolean verify(String text, String sign) {
		text = text + key;
		String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
		if (mysign.equals(sign)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param content
	 * @param charset
	 * @return
	 * @throws SignatureException
	 * @throws UnsupportedEncodingException
	 */
	private static byte[] getContentBytes(String content, String charset) {
		if (charset == null || "".equals(charset)) {
			return content.getBytes();
		}
		try {
			return content.getBytes(charset);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:"+ charset);
		}
	}
	
	/**
	 * 
	 * assemblySign:(生成订单签名).
	 * @author LiuJiangTao
	 * @param orderNo
	 * @param Money
	 * @return
	 */
	public static String assemblySign(String orderNo,float Money) {
		String signstr = orderNo + "," + Money;
		return sign(signstr);
	}
	
	/**
	 * resolve:(验证签名).
	 * @author LiuJiangTao
	 * @param orderNo 订单号
	 * @param Money 订单金额
	 * @param sign MD5码
	 * @return
	 */
	public static boolean resolve(String orderNo,float Money,String sign){
		String signstr = orderNo + "," + Money;
		return verify(signstr,sign);
	}
	
	/**
	 * callBackResolve:(验证请求是否合法).
	 * @author LiuJiangTao
	 * @param orderNo 订单号
	 * @param Money 交易金额（单精度）
	 * @param tradNo 交易号
	 * @param sign 回调密匙
	 * @return
	 */
	public static boolean callBackResolve(String orderNo,float Money,String tradNo,String sign){
		String signstr = orderNo + "," + Money + "," + tradNo;
		return verify(signstr,sign);
	}
	
	/**
	 * callBackAssemblySign:(生成回调密匙).
	 * @author LiuJiangTao
	 * @param orderNo 订单号
	 * @param Money 交易金额(单精度)
	 * @param tradNo 交易号
	 * @return
	 */
	public static String callBackAssemblySign(String orderNo,float Money,String tradNo){
		String signstr = orderNo + "," + Money + "," + tradNo;
		return sign(signstr); 
	}
}
