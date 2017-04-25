package com.pay.center.newPay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.collect.Maps;
import com.pay.coin.model.MemberCoin;
import com.pay.coin.model.MemberTradInfo;
import com.pay.coin.service.IMemberCoinService;
import com.pay.coin.service.IMemberTradInfoService;
import com.pay.common.CommonContrller;
import com.pay.common.utils.AlipayUtil;
import com.pay.common.utils.UtilPay;

/**
 * CoinPayController:{库币支付业务处理}
 * date: 2015-11-2 下午2:25:59 
 * @author LiuJiangTao
 * @version
 */
@Controller
@RequestMapping("/coinPay")
public class CoinPayController extends CommonContrller {

    @Autowired
    private IMemberTradInfoService memberTradInfoService;
    @Autowired
    private IMemberCoinService iMemberCoinService;
    protected Logger log = Logger.getLogger(CoinPayController.class);

    /**
     * immediatelyCoinPay:(库币支付业务处理).
     * @author LiuJiangTao
     * @param mav
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/immediatelyCoinPay")
    public ModelAndView immediatelyCoinPay(ModelAndView mav, HttpServletRequest request) {
        Map<?, ?> map = request.getParameterMap();
        String account="";
		// 组装请求参数
		Map<String, String> sPra = UtilPay.payReturnParamsFormat(map, null);
		String sign = sPra.remove("sign");
		String payPassword = sPra.remove("payPassword");
		//验证请求
		if (!sign.equals(UtilPay.assemblySign(sPra, AlipayUtil.key))) {
			log.debug("验证失败>>>>>>>>>>>>>>>>>>>>>>");
			mav.setViewName("payerror");
			return mav;
		}
//		payPassword = PassWordMd5Util.MD5(account, payPassword);
//		//判断用户填写支付密码是否正确
//		boolean flag = dubboMemberAllowableService.checkePayPassword(memberId,payPassword);
		if(!true){
			mav.getModel().put("result", "支付密码不正确，请重新支付！");
			mav.setViewName("notifyurl");
			return mav;
		}
		MemberTradInfo tradInfo = memberTradInfoService.getTradInfoById(Integer.valueOf(sPra.get("id")));
		// 查询库币信息
        MemberCoin memberCoin = iMemberCoinService.getMemberCoinById(12121);
        BigDecimal coinBalance = (memberCoin != null) ? memberCoin.getCoinBalance() : new BigDecimal("0");
		// 判断当前库币余额是否够支付
		boolean balancelack = coinBalance.compareTo(tradInfo.getProductMoney()) >= 0;
		if(!balancelack){
			mav.getModel().put("result", "当前库币余额不足，请充值！");
			mav.setViewName("notifyurl");
			return mav;
		}
        if (tradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
        	String detail = sPra.get("detail");
        	int detailMode = Integer.parseInt(sPra.get("detailMode"));
        	int detailType = Integer.parseInt(sPra.get("detailType"));
        	BigDecimal consume = tradInfo.getProductMoney();
            //扣掉库币
            iMemberCoinService.reduceCoin(12312, consume, account,detail, detailMode, detailType);
            //把支付状态改变
            tradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
            tradInfo.setTradSuccessTime(new Date());
            tradInfo.setTradNum(String.valueOf(System.currentTimeMillis()));
            memberTradInfoService.update(tradInfo);
			memberCoin = iMemberCoinService.getMemberCoinById(12121);
            String balance = memberCoin.getCoinBalance().toString();
        }
        //回调链接
        Map<String, String> newHashMap = Maps.newHashMap();
		newHashMap.put("orderNo", tradInfo.getOrderNum());
		newHashMap.put("tradNo", tradInfo.getTradNum());
		newHashMap = UtilPay.assemblyCallBackPara(newHashMap,AlipayUtil.key);
		
        return new ModelAndView(new RedirectView(tradInfo.getReturnUrl()), newHashMap);
    }

}
