package com.pay.center.newPay.controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Maps;
import com.pay.coin.model.MemberCoin;
import com.pay.coin.model.MemberTradInfo;
import com.pay.coin.service.IMemberCoinService;
import com.pay.coin.service.IMemberTradInfoService;
import com.pay.common.CommonContrller;
import com.pay.common.alipay.AlipaySubmit;
import com.pay.common.utils.AlipayUtil;
import com.pay.common.utils.Constants;
import com.pay.common.utils.GetClient;
import com.pay.common.utils.UtilPay;

@Controller
public class PayFiterController extends CommonContrller {
	@Autowired
	private IMemberTradInfoService tradService;
    @Autowired
    private IMemberTradInfoService memberTradInfoService;
    @Autowired
    private IMemberCoinService iMemberCoinService;
    protected Logger log = Logger.getLogger(PayFiterController.class);
	/**
	 * warmUpPay:(支付服务预加载地址). 
	 * 由于CAS拦截转发问题，会造成post数据丢失，
	 * 通过本方法预先加载处理CAS拦截
	 * @author LiuJiangTao
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="WarmUpPay", method = RequestMethod.GET) 
	public void warmUpPay(HttpServletResponse response) throws IOException{
		response.getWriter().print("    ");
	}
	
	/**
	 * payFiter:(用户过滤和分发支付请求).
	 * @param request
	 * @param memberTradInfo
	 * @param mav
	 * @author LiuJiangTao
	 * @return
	 */
	@RequestMapping(value="Pay", method = RequestMethod.POST) 
	public ModelAndView payFiter(HttpServletRequest request,ModelAndView mav,
			RedirectAttributes attr,MemberTradInfo trad) {
		long memberId = 121212;
		String account = "";
		// 验证请求是否合法
		log.debug("开始验证请求是否合法>>>>>>>>>>>>>>>>>>>>>>");
		if (!UtilPay.resolvePara(request,AlipayUtil.key)) {
			log.debug("验证失败>>>>>>>>>>>>>>>>>>>>>>");
			mav.setViewName("payerror");
			return mav;
		}
		Map<?, ?> map = request.getParameterMap();
		// 组装请求参数
		Map<String, String> sPra = UtilPay.payReturnParamsFormat(map, null);
		//判断参数是否缺少参数
		log.debug("开始验证参数是否为空>>>>>>>>>>>>>>>>>>>>>>");
		for (Map.Entry<String, String> entry : sPra.entrySet()) {
			if (StringUtils.isBlank(entry.getValue())) {
				if (entry.getKey().equals("productUrl"))
					continue;
				mav.getModel().put("result", entry.getKey() + "不能为空！");
				mav.setViewName("notifyurl");
				return mav;
			}else{
				if (entry.getKey().equals("productUrl")){
					if(!this.isImg(entry.getValue())){
						mav.getModel().put("result","商品图片链接非法" + entry.getValue() + "非法！");
						mav.setViewName("notifyurl");
						return mav;
					}
				}
			}
		}
		// 客户端的IP地址
		sPra.put("exter_invoke_ip", GetClient.IpAddr(request));
		sPra.put("notify_url", Constants.PAY_NOTIFY_URL);
		sPra.put("return_url", Constants.PAY_RETURN_URL);
		log.debug("sPra>>>>>>>>>>>>>>>>>>>>>>" + sPra.toString());
		trad.setMemberId(memberId);
		trad.setTradAccount(account);
		trad.setTradFlag(sPra.get("sign"));
		trad.setIp(sPra.get("exter_invoke_ip"));
		// 判断是否为重复请求，或者修改后请求
		MemberTradInfo m = tradService.getTradInfoByOrderNo(sPra.get("orderNum"),null);
		if (m == null) {
			long id = tradService.save(trad);
			trad.setId(id);
		}else{
			if (m.getTradStatus() == MemberTradInfo.TRADE_SATTUS_COMPLETE) {
				mav.getModel().put("result", "订单已支付，请刷新页面或联系客服处理");
				mav.setViewName("notifyurl");
				return mav;
			}
			trad.setId(m.getId());
			tradService.update(trad);
		}
		// 获取用户提交支付请求方式
		int payType = trad.getPayType();
		if (payType == MemberTradInfo.PAY_TYPE_NOCHECK_STATUS) {
			Map<String,String> parms = Maps.newHashMap();
			parms.put("id", String.valueOf(trad.getId()));
			parms.put("orderNum", trad.getOrderNum());
			parms.put("productName", trad.getProductName());
			parms.put("productMoney", trad.getProductMoney().toString());
			parms.put("detail", sPra.get("detail"));
			parms.put("detailMode",StringUtils.isBlank(sPra.get("detailMode")) ? "1" : sPra.get("detailMode"));
			parms.put("detailType",StringUtils.isBlank(sPra.get("detailType")) ? "1" : sPra.get("detailType"));
			parms = UtilPay.assemblyCallBackPara(parms,AlipayUtil.key);
			mav.getModelMap().putAll(parms);
			mav.setViewName("choosePayPage");
			return mav;
		} else if (payType == MemberTradInfo.PAY_TYPE_ALIPAY_STATUS) {
			// 如果用户选择支付宝支付
			// 组装页面信息
			Map<String, String> sParaTemp = AlipayUtil.assemblyAlipayParams(sPra);
			// 建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
			// 添加请求信息到本地数据库
			mav.getModel().put("sHtmlText", sHtmlText);
			mav.setViewName("alipayapi");
			return mav;
		} else if (payType == MemberTradInfo.PAY_TYPE_COIN_STATUS) {
			Map<String,String> parms = Maps.newHashMap();
			parms.put("id",String.valueOf(trad.getId()));
			parms.put("detail",sPra.get("detail"));
			parms.put("detailMode",StringUtils.isBlank(sPra.get("detailMode")) ? "1" : sPra.get("detailMode"));
			parms.put("detailType",StringUtils.isBlank(sPra.get("detailType")) ? "1" : sPra.get("detailType"));
			parms = UtilPay.assemblyCallBackPara(parms,AlipayUtil.key);
			return goToCoinPay(parms, memberId, account);
		}
		mav.getModel().put("result", "未知的支付方式");
		mav.setViewName("notifyurl");
		return mav;
	}
	
	/**
	 * submitPay:(提交支付).
	 * @author LiuJiangTao
	 * @param request
	 * @param mav
	 * @return
	 */
	@RequestMapping(value="submitPay", method = RequestMethod.POST) 
	public ModelAndView submitPay(HttpServletRequest request,RedirectAttributes attr,ModelAndView mav){
		long memberId = 1212313;
		String account = "qweq";
		Map<?, ?> map = request.getParameterMap();
		// 组装请求参数
		Map<String, String> sPra = UtilPay.payReturnParamsFormat(map, null);
		//获取用户支付方式
		Integer payType = Integer.parseInt(sPra.remove("kb-input"));
		//获取加密
		String sign = sPra.remove("sign");
		//验证请求
		if (!sign.equals(UtilPay.assemblySign(sPra, AlipayUtil.key))) {
			log.debug("验证失败>>>>>>>>>>>>>>>>>>>>>>");
			mav.setViewName("payerror");
			return mav;
		}
		MemberTradInfo m = tradService.getTradInfoById(Integer.parseInt(sPra.get("id")));
		if (m == null) {
			mav.getModel().put("result", "订单不存在或丢失,请联系客服");
			mav.setViewName("notifyurl");
			return mav;
		}
		m.setPayType(payType);
		tradService.update(m);
		if (payType == MemberTradInfo.PAY_TYPE_ALIPAY_STATUS) {
			sPra.put("exter_invoke_ip", GetClient.IpAddr(request));
			sPra.put("notify_url", Constants.PAY_NOTIFY_URL);
			sPra.put("return_url", Constants.PAY_RETURN_URL);
			Map<String, String> sParaTemp = AlipayUtil.assemblyAlipayParams(sPra);
			// 建立请求
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
			// 添加请求信息到本地数据库
			mav.getModel().put("sHtmlText", sHtmlText);
			mav.setViewName("alipayapi");
		}else{
			Map<String,String> parms = Maps.newHashMap();
			parms.put("id", sPra.get("id"));
			parms.put("detail", sPra.get("detail"));
			parms.put("detailMode",StringUtils.isBlank(sPra.get("detailMode")) ? "1" : sPra.get("detailMode"));
			parms.put("detailType",StringUtils.isBlank(sPra.get("detailType")) ? "1" : sPra.get("detailType"));
			parms = UtilPay.assemblyCallBackPara(parms,AlipayUtil.key);
			return goToCoinPay(parms, memberId, account);
		}
		return mav;
	}
	
	/**
     * goToCoinPay:(前往库币支付页面).
     */
	public ModelAndView goToCoinPay(Map<String, String> sPra, long memberId,String username) {
		ModelAndView mav = new ModelAndView();
		// 查询交易信息
		MemberTradInfo tradInfo = memberTradInfoService.getTradInfoById(Integer.valueOf(sPra.get("id")));
		// 查询库币信息
		MemberCoin memberCoin = iMemberCoinService.getMemberCoinById(memberId);
		// 查询是否设置了支付密码
		boolean checkePayPassword = true;
		// 获取库币余额
		BigDecimal coinBalance = (memberCoin != null) ? memberCoin.getCoinBalance() : new BigDecimal("0");
		// 判断当前库币余额是否够支付
		boolean balancelack = coinBalance.compareTo(tradInfo.getProductMoney()) >= 0;
		
		mav.getModelMap().putAll(sPra);
		mav.getModel().put("username", username);
		mav.getModel().put("coinBalance", coinBalance);
		mav.getModel().put("balancelack", balancelack);
		mav.getModel().put("checkePayPassword", checkePayPassword);
		mav.getModel().put("tradInfo", tradInfo);
		mav.setViewName("coinPayPage");
		return mav;
	}
	
	
	
	/**
	 * checkPayPassword:(监测支付密码是否正确).
	 * @author LiuJiangTao
	 * @param payPassword
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping("/checkPayPassword")
	public boolean checkPayPassword(@RequestParam("param") String payPassword,HttpServletRequest request) {
    	long memberId = 1212313;
		String account = "qweq";
		boolean flag = true;
		return flag;
	}
	/**
	 * isImg:(判断链接是否为图片).
	 * @author LiuJiangTao
	 * @param url 图片链接
	 * @return
	 */
	public boolean isImg(String url) {
		try {
			Image img = ImageIO.read(new File(url));
			return img != null;
		} catch (Exception ex) {
			return false;
		}
	}
}
