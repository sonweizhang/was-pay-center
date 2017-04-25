package com.pay.center.controller;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.google.common.collect.Maps;
import com.pay.coin.model.MemberCoin;
import com.pay.coin.model.MemberTradInfo;
import com.pay.coin.service.IMemberCoinService;
import com.pay.coin.service.IMemberTradInfoService;
import com.pay.common.CommonContrller;

/**
 * @param
 * @version V1.0
 * @Copyright 北京网库互联信息技术有限公司
 * @Title:MemberCoinController.java
 * @Description:库币支付业务处理controller
 * @author：chenwenning
 * @Modified chenwenning
 * @date 2015-5-21 上午10:34:25
 */
@Controller
@RequestMapping("/memberCoin")
public class MemberCoinController {

//    @Autowired
//    private IMemberTradInfoService memberTradInfoService;
//    @Autowired
//    private IMemberCoinService iMemberCoinService;
//
//    @ResponseBody
//    @RequestMapping("/isCheckingPayPassword")
//    public boolean isCheckingPayPassword(@RequestParam("param") String payPassword, HttpServletRequest request) {
//        // 加密密码
//        boolean flag = true;
//        return flag;
//    }
//
//    /**
//     * @param memberTradInfo 提交过来的实体信息
//     * @return ModelAndView
//     * @throws UnsupportedEncodingException
//     * @throws
//     * @Title: goToPay
//     * @Description:选择支付方式 接收提交过来的信息
//     * @author chenwenning
//     * @since 1.0
//     */
//    @RequestMapping("/goToCoinPay")
//    public ModelAndView goToCoinPay(ModelAndView md, @RequestParam("id") long id
//            , @RequestParam("detail") String detail
//            , @RequestParam("detailMode") int detailMode
//            , @RequestParam("detailType") int detailType
//            , @RequestParam("tradFlag") String tradFlag
//            , HttpServletRequest request) throws UnsupportedEncodingException {
//        MemberTradInfo tradInfo = memberTradInfoService.getTradInfoById(id);
//        float money = tradInfo.getProductMoney().floatValue();
//        //确认订单未修改
//		if (!WkSign.resolve(tradInfo.getOrderNum(),money,tradFlag)) {
//			md.setViewName("payerror");
//			return md;
//		}
//        md.getModel().put("detail",AliPayUtil.strFormatBySystem(detail));
//        md.getModel().put("detailMode", detailMode);
//        md.getModel().put("detailType", detailType);
//        md.getModel().put("tradInfo", tradInfo);
//        md.getModel().put("username", username);
//        md = goPay(md, request);
//        md.setViewName("likeToCoinPay");
//        return md;
//    }
//
//    private ModelAndView goPay(ModelAndView md, HttpServletRequest req) {
//        long memberId = this.getMemberId(req);
//        //查询库币的余额
//        MemberCoin memberCoin = iMemberCoinService.getMemberCoinById(memberId);
//        if (memberCoin != null) {
//            md.getModel().put("coinBalance", memberCoin.getCoinBalance());
//        } else {
//            md.getModel().put("coinBalance", 0);
//        }
//        //查询是否设置了支付密码
//        boolean checkePayPassword = dubboMemberAllowableService.checkePayPassword(memberId);
//        md.getModel().put("checkePayPassword", checkePayPassword);
//        return md;
//    }
//
//    /**
//     * immediatelyCoinPay
//     *
//     * @param md
//     * @param PayCoin
//     * @param tradId
//     * @param detail
//     * @param detailMode
//     * @param detailType
//     * @param req
//     * @return ModelAndView
//     * @throws
//     * @Description:
//     * @author chenwenning
//     * @since 1.0
//     */
//    @RequestMapping("/immediatelyCoinPay")
//    @ResponseBody
//    public ModelAndView immediatelyCoinPay(ModelAndView md
//            , @RequestParam("tradId") String tradId
//            , @RequestParam("detail") String detail
//            , @RequestParam("detailMode") int detailMode
//            , @RequestParam("detailType") int detailType
//            , @RequestParam("tradFlag") String tradFlag
//            , HttpServletRequest req) {
//        long memberId = this.getMemberId(req);
//        int siteId = this.getMemberSiteIdByAcconut(req);
//        MemberTradInfo tradInfo = memberTradInfoService.getTradInfoById(Long.valueOf(tradId));
//        float money = tradInfo.getProductMoney().floatValue();
//        String orderNo = tradInfo.getOrderNum();
//        //确认订单未修改
//        if (!WkSign.resolve(orderNo,money,tradFlag)) {
//			md.setViewName("payerror");
//			return md;
//		}
//        if (tradInfo.getTradStatus() != MemberTradInfo.TRADE_SATTUS_COMPLETE) {
//            //扣掉库币
//            iMemberCoinService.reduceCoin(memberId, tradInfo.getProductMoney(), this.getCurrentLoginUsername(req), detail, detailMode, detailType);
//            //把支付状态改变
//            tradInfo.setTradStatus(MemberTradInfo.TRADE_SATTUS_COMPLETE);
//            tradInfo.setTradSuccessTime(new Date());
//            tradInfo.setTradNum(String.valueOf(System.currentTimeMillis()));
//            memberTradInfoService.update(tradInfo);
//            iRDubboMemberMsgcenterService.sendDiyParamSysMsg(memberId, siteId, "", "", "", "", 5);
//        }
//        String sign = WkSign.callBackAssemblySign(orderNo,tradInfo.getProductMoney().floatValue(),tradInfo.getTradNum());
//        //回调链接
//        HashMap<String, String> newHashMap = Maps.newHashMap();
//		newHashMap.put("orderNo", orderNo);
//		newHashMap.put("tradNo", tradInfo.getTradNum());
//		newHashMap.put("sign", sign);
//        return new ModelAndView(new RedirectView(tradInfo.getReturnUrl()), newHashMap);
//    }

}
