package com.pay.center.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pay.common.CommonContrller;

@Controller
@RequestMapping("WkAlipay")
public class WkAlipayController extends CommonContrller {
//	@Autowired
//	private IMemberTradInfoService iMemberTradInfoService;
//
//	@Autowired
//	private IMemberAlipayReturnService iMemberAlipayReturnService;
//	
//	
//	/**
//	 * 
//	 * alipay:(用户选择支付宝方式付款).
//	 * @author LiuJiangTao
//	 * @param orderid
//	 * @param request
//	 * @param mav
//	 * @return
//	 */
//	@RequestMapping("/alipay/{orderid}/{tradFlag}")
//	public ModelAndView alipay(@PathVariable String orderid,@PathVariable String tradFlag,
//			HttpServletRequest request, ModelAndView mav) {
//		MemberTradInfo tradInfo = iMemberTradInfoService.getTradInfoById(Long.valueOf(orderid));
//		if (tradInfo != null && tradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
//			//确认订单未修改
//			float money = tradInfo.getProductMoney().floatValue();
//			if (!WkSign.resolve(tradInfo.getOrderNum(), money, tradFlag)) {
//				mav.setViewName("payerror");
//				return mav;
//			}
//			// 如果用户选择支付宝支付
//			// 组装页面信息
//			Map<String, String> sParaTemp = AliPayUtil.assemblyAlipayParams(request, tradInfo);
//			// 建立请求
//			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
//			// 添加请求信息到本地数据库
//			mav.getModel().put("sHtmlText", sHtmlText);
//			mav.setViewName("alipayapi");
//		}else{
//			mav.setViewName("payerror");
//		}
//		return mav;
//	}
//
//	/**
//	 * notifyUrl:(处理支付宝异步调用).
//	 * @author LiuJiangTao
//	 * @param request
//	 * @param mav
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/notifyUrl")
//	public ModelAndView notifyUrl(HttpServletRequest request, ModelAndView mav) throws Exception {
//		LogMgr.writeSysInfoLog("WkPay异步>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//		//定义异步回调处理结果
//		String result = null;
//		// 获取支付宝GET过来反馈信息
//		Map<String, String> params = UtilPay.getpayReturnParamsForVerify(request);
//		LogMgr.writeSysInfoLog("支付宝返回参数:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + params);
//		boolean verify_result = AlipayNotify.verify(params);
//		if (verify_result) {
//			LogMgr.writeSysInfoLog("验证通过>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			//当前订单号
//			String orderNo = params.get("out_trade_no");
//			//当前交易号
//			String tradNo = params.get("trade_no");
//			// 获取判断本地是否已存在订单支付信息
//			MemberAlipayReturn alipayReturn = AliPayUtil.assemblyAlipayReturn(params,MemberAlipayReturn.RETURN_TYPE_YB);
//			// 根据返回订单号获取订单详情
//			MemberTradInfo memberTradInfo = iMemberTradInfoService.getTradInfoByOrderNo(orderNo,null);
//			// 如果订单不存在为空，则返回到异常页。
//			if (memberTradInfo == null) {
//				// 定义返回给支付宝页面
//				mav.setViewName("notifyurl");
//				return mav;
//			}
//			// 保存当前支付类型
//			alipayReturn.setAlipayType(memberTradInfo.getTradType());
//			// 验证成功
//			String trade_status = alipayReturn.getTradeStatus();
//			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
//				LogMgr.writeSysInfoLog("交易成功>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//				//判断是否处理过业务
//				if (memberTradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
//					LogMgr.writeSysInfoLog("开始处理业务>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//					alipayReturn.setIsSuccess(MemberAlipayReturn.IS_SUCCESS);
//					memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
//					memberTradInfo.setTradSuccessTime(new Date());
//					memberTradInfo.setPayType(MemberTradInfo.PAY_TYPE_ALIPAY_STATUS);
//					memberTradInfo.setTradNum(tradNo);
//					iMemberTradInfoService.update(memberTradInfo);
//					iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
//				}
//				String sign = WkSign.callBackAssemblySign(orderNo,alipayReturn.getTotalFee().floatValue(),tradNo);
//				//定义下级转发参数
//				Map<String,String> sPra = new HashMap<String,String>();
//				sPra.put("orderNo",orderNo);
//				sPra.put("tradNo",tradNo);
//				sPra.put("sign",sign);
//				LogMgr.writeSysInfoLog("开始异步回调>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+sPra);
//				result = toNotifyUrl(memberTradInfo.getNotifyUrl(),sPra);
//				LogMgr.writeSysInfoLog("异步回调完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			}else{
//				alipayReturn.setIsSuccess(MemberAlipayReturn.IS_FAILED);
//				memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_PAY);
//				result = "fail";
//			}
//		} else {
//			LogMgr.writeSysInfoLog("WkPay异步认证失败>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			result = "fail";
//		}
//		mav.getModel().put("result", result);
//		return mav;
//	}
//
//	/**
//	 * 
//	 * toNotifyUrl:(模拟HTTP请求).
//	 * @author LiuJiangTao
//	 * @param url
//	 * @param map
//	 * @return
//	 */
//	public static String toNotifyUrl(String url, Map<String, String> map) {
//		try {
//			Result result = SendRequest.sendPost(url, null, map, "UTF-8");
//			String entity = EntityUtils.toString(result.getHttpEntity(),"UTF-8");
//			LogMgr.writeSysInfoLog("entity-------------------------"+entity);
//			return entity.contains("success") ? entity : "fail";
//		} catch (Exception e) { 
//			LogMgr.writeErrorLog(e);
//			return "";
//		}
//	}
//	
//	/**
//	 * 
//	 * returnUrl:(处理支付同步调用).
//	 * @author LiuJiangTao
//	 * @param request
//	 * @param mav
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/returnUrl")
//	public ModelAndView returnUrl(HttpServletRequest request, ModelAndView mav) throws Exception {
//		LogMgr.writeSysInfoLog("WkPay同步回调开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//		// 获取支付宝GET过来反馈信息
//		Map<String, String> params = UtilPay.getpayReturnParamsForVerify(request);
//		LogMgr.writeSysInfoLog("支付宝返回参数:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + params);
//		boolean verify_result = AlipayNotify.verify(params);
//		if (verify_result) {
//			LogMgr.writeSysInfoLog("WkPay同步回调验证通过>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			//当前订单号
//			String orderNo = params.get("out_trade_no");
//			//当前交易号
//			String tradNo = params.get("trade_no");
//			// 根据返回订单号获取订单详情
//			MemberTradInfo memberTradInfo = iMemberTradInfoService.getTradInfoByOrderNo(orderNo,null);
//			// 如果订单不存在为空，则返回到异常页。
//			if (memberTradInfo == null) {
//				LogMgr.writeSysInfoLog("订单不存在,请联系客服:>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//				mav.getModel().put("result", "订单不存在,请联系客服。");
//				mav.setViewName("notifyurl");
//				return mav;
//			}
//			// 获取支付宝返回信息实体格式
//			MemberAlipayReturn alipayReturn = AliPayUtil.assemblyAlipayReturn(params,MemberAlipayReturn.RETURN_TYPE_TB);
//			// 保存当前支付类型
//			alipayReturn.setAlipayType(memberTradInfo.getTradType());
//			//获取订单状态
//			String trade_status = alipayReturn.getTradeStatus();
//			if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
//				LogMgr.writeSysInfoLog("WkPay订单支付完成>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//				//判断是否处理过业务
//				if (memberTradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
//					// 如果订单为
//					alipayReturn.setIsSuccess(MemberAlipayReturn.IS_SUCCESS);
//					memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
//					memberTradInfo.setTradSuccessTime(new Date());
//					memberTradInfo.setPayType(MemberTradInfo.PAY_TYPE_ALIPAY_STATUS);
//					memberTradInfo.setTradNum(tradNo);
//					iMemberTradInfoService.update(memberTradInfo);
//					iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
//				}
//				String sign = WkSign.callBackAssemblySign(orderNo,alipayReturn.getTotalFee().floatValue(),tradNo);
//				//定义下级转发参数
//		        HashMap<String, String> sPra = Maps.newHashMap();
//		        sPra.put("orderNo",orderNo);
//		        sPra.put("tradNo",tradNo);
//		        sPra.put("sign",sign);
//				LogMgr.writeSysInfoLog("WkPay订单支付完成开始回调>>>>>>>>>>>>>>>>>>>>>"+sPra);
//				return new ModelAndView(new RedirectView(memberTradInfo.getReturnUrl()),sPra);
//			}else{
//				LogMgr.writeSysInfoLog("WkPay-------------------------returnUrl----FAILD");
//				alipayReturn.setIsSuccess(MemberAlipayReturn.IS_FAILED);
//				memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_PAY);
//				return mav;
//			}
//		} else {
//			LogMgr.writeSysInfoLog("WkPay同步回调验证异常>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
//			mav.getModel().put("result", "支付宝回调验证异常，请联系客服。");
//			mav.setViewName("notifyurl");
//			return mav;
//		}
//	}
}
