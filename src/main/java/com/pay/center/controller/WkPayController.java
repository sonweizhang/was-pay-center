package com.pay.center.controller;

import org.springframework.stereotype.Controller;

import com.pay.common.CommonContrller;

@Controller
public class WkPayController extends CommonContrller {
//	@Autowired
//	private IMemberTradInfoService iMemberTradInfoService;
//	/**
//	 * WkpayFiter:(用户过滤和分发支付请求).
//	 * @param request
//	 * @param memberTradInfo
//	 * @param mav
//	 * @param detail 充值详情
//	 * @param detailMode 充值方式
//	 * @param detailType 记录方式  充值 消费 活动
//	 * @author LiuJiangTao
//	 * @return
//	 */
//	@RequestMapping("WkPay")
//	public ModelAndView WkpayFiter(HttpServletRequest request,
//			MemberTradInfo memberTradInfo,ModelAndView mav
//			,@RequestParam(value="detail",required=false,defaultValue="")String detail
//			,@RequestParam(value="detailMode",required=false,defaultValue="1")int detailMode
//			,@RequestParam(value="detailType",required=false,defaultValue="1")int detailType
//			,RedirectAttributes attr) {
//		mav.setViewName("payerror");
//		//获取当前登陆用户ID
//		long memberId = this.getMemberId(request);
//		//获取当前登陆用户用户名
//		String account = this.getCurrentLoginUsername(request);
//		//中文转码
//		detail = AliPayUtil.strFormatBySystem(detail);
//		memberTradInfo.setProductName(AliPayUtil.strFormatBySystem(memberTradInfo.getProductName()));
//		memberTradInfo.setProductDescription(AliPayUtil.strFormatBySystem(memberTradInfo.getProductDescription()));
//		//获取用户提交订单
//		String orderNo = memberTradInfo.getOrderNum();
//		//获取订单金额
//		float money = memberTradInfo.getProductMoney().floatValue();
//		//判断是否为非法请求
//		String tradFlag = memberTradInfo.getTradFlag();
//		System.out.println("tradFlag--------validation---start:"+tradFlag);
//		if(!WkSign.resolve(orderNo, money,tradFlag))
//			return mav;
//		System.out.println("tradFlag--------validation---end:"+tradFlag);
//		memberTradInfo.setMemberId(memberId);
//		memberTradInfo.setTradAccount(account);
//		//判断是否为重复请求，或者修改后请求
//		MemberTradInfo m = iMemberTradInfoService.getTradInfoByOrderNo(orderNo, null);
//		if (m != null) {
//			if(m.getTradStatus() == MemberTradInfo.TRADE_SATTUS_COMPLETE){
//				//如果订单信息已存在并且为支付订单号,则返回
//				return mav;
//			}
//			if(m.getTradFlag().equals(memberTradInfo.getTradFlag())){
//				//如果订单未支付且加密串一致，则取库中信息
//				memberTradInfo.setId(m.getId());
//				iMemberTradInfoService.update(memberTradInfo);
//			}
//		}else{
//			//如果订单 号不存在则添加订单
//			long id = iMemberTradInfoService.save(memberTradInfo);
//			memberTradInfo.setId(id);
//		}
//		//获取用户提交支付请求方式
//		int payType = memberTradInfo.getPayType();
//		if (payType == MemberTradInfo.PAY_TYPE_NOCHECK_STATUS) {
//			// 如果用户未选择支付方式
//			mav.getModel().put("memberTradInfo",memberTradInfo);
//			mav.getModel().put("detail", detail);
//			mav.getModel().put("detailMode", detailMode);
//			mav.getModel().put("detailType", detailType);
//			mav.setViewName("likeToPay");
//		} else if (payType == MemberTradInfo.PAY_TYPE_ALIPAY_STATUS) {
//			// 如果用户选择支付宝支付
//			// 组装页面信息
//			Map<String,String> sParaTemp = AliPayUtil.assemblyAlipayParams(request, memberTradInfo);
//			// 建立请求
//			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
//			//添加请求信息到本地数据库
//			mav.getModel().put("sHtmlText", sHtmlText);
//			mav.setViewName("alipayapi");
//		}else if(payType == MemberTradInfo.PAY_TYPE_COIN_STATUS){
//			// 如果用户选择库币支付
//			attr.addAttribute("id",memberTradInfo.getId());
//			attr.addAttribute("tradFlag",tradFlag);
//			attr.addAttribute("detail", detail);
//			attr.addAttribute("detailMode", detailMode);
//			attr.addAttribute("detailType", detailType);
//			return new ModelAndView("redirect:/memberCoin/goToCoinPay"); 
//		}
//		return mav;
//	}
}
