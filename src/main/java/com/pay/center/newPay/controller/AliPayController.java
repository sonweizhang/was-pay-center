package com.pay.center.newPay.controller;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.collect.Maps;
import com.pay.coin.model.MemberAlipayReturn;
import com.pay.coin.model.MemberTradInfo;
import com.pay.coin.service.IMemberAlipayReturnService;
import com.pay.coin.service.IMemberTradInfoService;
import com.pay.common.CommonContrller;
import com.pay.common.alipay.AliPayUtil;
import com.pay.common.alipay.AlipayNotify;
import com.pay.common.utils.AlipayUtil;
import com.pay.common.utils.UtilPay;

@Controller
@RequestMapping("aliPay")
public class AliPayController extends CommonContrller {
	@Autowired
	private IMemberTradInfoService iMemberTradInfoService;
	@Autowired
	private IMemberAlipayReturnService iMemberAlipayReturnService;
    protected Logger log = Logger.getLogger(AliPayController.class);
    /**
	 * notifyUrl:(处理支付宝异步调用).
	 * @author LiuJiangTao
	 * @param request
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/notifyUrl")
	public ModelAndView notifyUrl(HttpServletRequest request, ModelAndView mav) throws Exception {
		log.debug("NotifyUrl>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = UtilPay.getpayReturnParamsForVerify(request);
		log.debug("params>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + params);
		boolean verify_result = AlipayNotify.verify(params);
		if (!verify_result) {
			log.debug("Validation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>error");
			mav.setViewName("notify_url");
			return mav;
		}
		log.debug("Validation>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>success");
		//当前订单号
		String orderNo = params.get("out_trade_no");
		//当前交易号
		String tradNo = params.get("trade_no");
		// 获取判断本地是否已存在订单支付信息
		MemberAlipayReturn alipayReturn = AliPayUtil.assemblyAlipayReturn(params,MemberAlipayReturn.RETURN_TYPE_YB);
		// 根据返回订单号获取订单详情
		MemberTradInfo memberTradInfo = iMemberTradInfoService.getTradInfoByOrderNo(orderNo,null);
		// 如果订单不存在为空，则返回到异常页。
		if (memberTradInfo == null) {
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>订单不存在");
			mav.setViewName("notifyurl");
			return mav;
		}
		//定义回调地址
		String redirectUrl = memberTradInfo.getNotifyUrl();
		// 保存当前支付类型
		alipayReturn.setAlipayType(memberTradInfo.getTradType());
		// 验证成功
		String trade_status = alipayReturn.getTradeStatus();
		if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
			log.debug("TRADE_SUCCESS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			//判断是否处理过业务
			if (memberTradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
				log.debug("DEAL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				alipayReturn.setIsSuccess(MemberAlipayReturn.IS_SUCCESS);
				memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
				memberTradInfo.setTradSuccessTime(new Date());
				memberTradInfo.setPayType(MemberTradInfo.PAY_TYPE_ALIPAY_STATUS);
				memberTradInfo.setTradNum(tradNo);
				iMemberTradInfoService.update(memberTradInfo);
				iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
			}
			log.debug("DEAL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Finash");
			//定义下级转发参数
			Map<String,String> newHashMap = Maps.newHashMap();
			newHashMap.put("orderNo",orderNo);
			newHashMap.put("tradNo",tradNo);
			log.debug("CallBack>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Start");
			String result = UtilPay.sendPostInfo(newHashMap, redirectUrl, AlipayUtil.key);
			mav.getModel().put("result", result);
			log.debug("CallBack>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>End");
		}else{
			alipayReturn.setIsSuccess(MemberAlipayReturn.IS_FAILED);
			iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
		}
		mav.setViewName("notifyurl");
		return mav;
	}
	
	/**
	 * returnUrl:(处理支付同步调用).
	 * @author LiuJiangTao
	 * @param request
	 * @param mav
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/returnUrl")
	public ModelAndView returnUrl(HttpServletRequest request, ModelAndView mav) throws Exception {
		log.debug("Return>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// 获取支付宝过来反馈信息
		Map<String, String> params = UtilPay.getpayReturnParamsForVerify(request);
		log.debug("params>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + params);
		boolean verify_result = AlipayNotify.verify(params);
		if (!verify_result) {
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>支付宝回调验证失败");
			mav.setViewName("payerror");
			return mav;
		}
		log.debug("PASSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//当前订单号
		String orderNo = params.get("out_trade_no");
		//当前交易号
		String tradNo = params.get("trade_no");
		// 获取支付宝返回信息实体格式
		MemberAlipayReturn alipayReturn = AliPayUtil.assemblyAlipayReturn(params,MemberAlipayReturn.RETURN_TYPE_TB);
		// 根据返回订单号获取订单详情
		MemberTradInfo memberTradInfo = iMemberTradInfoService.getTradInfoByOrderNo(orderNo,null);
		// 如果订单不存在为空，则返回到异常页。
		if (memberTradInfo == null) {
			log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>订单不存在");
			mav.setViewName("payerror");
			return mav;
		}
		//获取订单状态
		String trade_status = alipayReturn.getTradeStatus();
		if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
			log.debug("TRADE_SUCCESS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			//判断是否处理过业务
			if (memberTradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
				log.debug("DEAL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				// 如果订单为
				alipayReturn.setIsSuccess(MemberAlipayReturn.IS_SUCCESS);
				memberTradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
				memberTradInfo.setTradSuccessTime(new Date());
				memberTradInfo.setPayType(MemberTradInfo.PAY_TYPE_ALIPAY_STATUS);
				memberTradInfo.setTradNum(tradNo);
				alipayReturn.setAlipayType(memberTradInfo.getTradType());
				iMemberTradInfoService.update(memberTradInfo);
				iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
			}
			log.debug("DEAL>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Finash");
			//定义下级转发参数
	        Map<String, String> newHashMap = Maps.newHashMap();
			newHashMap.put("orderNo",orderNo);
			newHashMap.put("tradNo",tradNo);
			newHashMap = UtilPay.assemblyCallBackPara(newHashMap,AlipayUtil.key);
			log.debug("parms>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + newHashMap);
			return new ModelAndView(new RedirectView(memberTradInfo.getReturnUrl()),newHashMap);
		}else{
			alipayReturn.setIsSuccess(MemberAlipayReturn.IS_FAILED);
			iMemberAlipayReturnService.saveMemberAlipayReturn(alipayReturn);
		}
		return mav;
	}
}
