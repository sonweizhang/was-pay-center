package com.pay.common.alipay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.pay.coin.model.MemberAlipayReturn;
import com.pay.coin.model.MemberTradInfo;
import com.pay.common.utils.Constants;
import com.pay.common.utils.DateUtil;
import com.pay.common.utils.UtilPay;


/**
 * 
 * AliPayUtil:{支付宝辅助类}
 * date: 2015-5-20 下午3:40:21 
 * @author LiuJiangTao
 * @version 
 */
public class AliPayUtil {
	
	public static Map<String, String> assemblyAlipayParams(HttpServletRequest request,MemberTradInfo m){
		//异步站内回调
		String notify_url = Constants.ALIPAY_NOTIFY_URL;
		//同步站内回调
		String return_url = Constants.ALIPAY_RETURN_URL;
		// 防钓鱼时间戳
		String anti_phishing_key = "";
		// 客户端的IP地址
		String exter_invoke_ip = request.getRemoteAddr();
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("payment_type","1");
		sParaTemp.put("notify_url",notify_url);
		sParaTemp.put("return_url",return_url);
		sParaTemp.put("seller_email",AlipayConfig.seller_email);
		sParaTemp.put("out_trade_no", m.getOrderNum());
		sParaTemp.put("subject",m.getProductName());
		sParaTemp.put("total_fee",String.valueOf(m.getProductMoney().floatValue()));
		sParaTemp.put("body",  m.getProductDescription());
		sParaTemp.put("show_url", "");
		sParaTemp.put("anti_phishing_key", anti_phishing_key);
		sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
		return sParaTemp;
	}
	
	
	/**
	 * 
	 * assemblyAlipayReturn:(组装支付宝返回数据).
	 * @author LiuJiangTao
	 * @param request
	 * @return
	 */
	public static MemberAlipayReturn assemblyAlipayReturn(Map<String,String> map,int return_type) {
			MemberAlipayReturn m = new MemberAlipayReturn();
			m.setBody(map.get("body"));
			m.setBuyerEmail(map.get("buyer_email"));
			m.setBuyerId(map.get("buyer_id"));
			m.setNotifyId(map.get("notify_id"));
			m.setNotifyTime(DateUtil.parseStringToDate(map.get("notify_time"), "yyyy-MM-dd HH:mm:ss"));
			m.setNotifyType(map.get("notify_type"));
			m.setOrderNo(map.get("out_trade_no"));
			m.setBuyerAccount(map.get("buyer_email"));
			m.setPrice(new BigDecimal(map.get("total_fee")));
			m.setSellerId(map.get("seller_id"));
			m.setSign(map.get("sign"));
			m.setSubject(map.get("subject"));
			m.setTotalFee(new BigDecimal(map.get("total_fee")));
			m.setTradeNo(map.get("trade_no"));
			m.setTradeStatus( map.get("trade_status"));
			m.setReturnType(return_type);
	        m.setAddTime(new Date());
	        m.setBuyerActions("");
	        m.setBuyerType("");
	        m.setCreateTime(new Date());
	        m.setCurrency("");
	        m.setDiscount(new BigDecimal(0));
	        m.setGatheringType("");
	        m.setGmtCreate(new Date());
	        m.setGmtPayment(new Date());
	        m.setInputCharset(AlipayConfig.input_charset);
	        m.setIsTotalFeeAdjust("");
	        m.setModifiedTime(new Date());
	        m.setOperatorRole("");
	        m.setPartner(AlipayConfig.partner);
	        m.setQuantity(1);
	        m.setRemark("");
	        m.setSellerActions("");
	        m.setSellerType("");
	        m.setSiteId(1);
	        m.setTradeType("");
	        m.setUseCoupon("");
	        m.setSellerEmail(AlipayConfig.seller_email);
	        m.setSignType(AlipayConfig.sign_type);
	        m.setService("create_direct_pay_by_user");
			return m;
	}
	
	/**
	 * 
	 * strFormatBySystem:(这里用一句话描述这个方法的作用).
	 * @author LiuJiangTao
	 * @param str
	 * @return
	 */
	public static String strFormatBySystem(String str) {
		// 获取当前系统版本
		String OS = System.getProperty("os.name").toLowerCase();
		return OS.contains("windows") ? UtilPay.StrFormat(str,UtilPay.input_charset) : str;
	}
}
