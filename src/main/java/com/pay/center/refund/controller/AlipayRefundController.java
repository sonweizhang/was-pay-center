package com.pay.center.refund.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.pay.coin.model.MemberAlipayRefundLog;
import com.pay.coin.model.MemberTradInfo;
import com.pay.coin.service.IMemberTradInfoService;
import com.pay.common.alipay.AlipayNotify;
import com.pay.common.alipay.AlipaySubmit;
import com.pay.common.utils.AlipayUtil;
import com.pay.common.utils.Constants;
import com.pay.common.utils.UtilDate;
import com.pay.common.utils.UtilPay;
import com.pay.refund.service.IMemberAlipayRefundLogService;

/**
 * WkAlipayRefundController:{处理支付宝退款业务} 
 * date: 2015-10-15 下午7:25:58
 * @author LiuJiangTao
 * @version
 */
@Controller
@RequestMapping("alipayRefund")
public class AlipayRefundController{

	@Autowired
	private IMemberTradInfoService iMemberTradInfoService;
	@Autowired
	private IMemberAlipayRefundLogService iMemberRefundService;
	
    protected Logger log = Logger.getLogger(AlipayRefundController.class);
	/**
	 * toRefund:(发送退款请求).
	 * @author LiuJiangTao
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="toRefund", method = RequestMethod.POST,produces = "application/json;charset=utf-8")  
	public String toRefund(HttpServletRequest request) {
		String success = "{\"info\":\"操作成功!\",\"status\":\"100\"}";
		String failed = "{\"info\":\"操作失败!\",\"status\":\"102\"}";
		String error = "{\"info\":\"操作异常!\",\"status\":\"103\"}";
		String illegal = "{\"info\":\"非法请求!\",\"status\":\"104\"}";
		log.debug("退款开始>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		if (!UtilPay.resolvePara(request,AlipayUtil.key)) {
			return illegal;
		}
		log.debug("验证通过>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		//获取请求参数集合
		Map<?, ?> map = request.getParameterMap();
		Map<String, String> params = UtilPay.payReturnParamsFormat(map, null);
		// 加密串
		String sign = params.get("sign");
		// 会员ID
		long memberId = Long.valueOf(params.get("memberId"));
		// 退款理由
		String reason = params.get("reason");
		// 异步回调地址
		String notifyUrl = params.get("notifyUrl");
		// 获取退款信息
		String detail = params.get("detail");
		// 获取操作人
		String operator = params.get("operator");
		// 监测退款信息是否正常
		if (!CheckDetailData(detail,memberId))
			return error;
		int batchnum = this.getSubNumber(detail, "#") + 1;
		// 生成批次号
		String batchno = UtilDate.getDate() + System.currentTimeMillis() + UtilDate.getThree();
		// 设置时间
		String refunddate = UtilDate.getDateFormatter();
		// 组装退款信息
		MemberAlipayRefundLog loger = new MemberAlipayRefundLog(reason, notifyUrl, operator,
				batchno, detail, batchnum,
				UtilDate.getDatePaser(refunddate), sign);
		log.debug("保存记录>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		// 记录退款申请
		iMemberRefundService.save(loger);
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("detail_data", detail);
		sParaTemp.put("batch_no", batchno);
		sParaTemp.put("batch_num", String.valueOf(batchnum));
		sParaTemp.put("refund_date", refunddate);
		sParaTemp.put("notify_url", Constants.REFUND_NOTIFY_URL);
		sParaTemp = AlipayUtil.assemblyAlipayRefundParams(sParaTemp);
		try {
			String result = AlipaySubmit.sendPostInfo(sParaTemp);
			log.debug("退款结果>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result);
			Document doc = Jsoup.parse(result);
			String is_success = doc.getElementsByTag("is_success").first().text();
			if(is_success.equals("T")){
				return success;
			}else {
				return failed;
			}
		} catch (Exception e) {
			log.debug(e);
			return failed;
		}
	}

	/**
	 * notifyUrl:(处理退款支付宝回调).
	 * @author LiuJiangTao
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/notifyUrl")
	public ModelAndView notifyUrl(HttpServletRequest request,ModelAndView mav) throws Exception {
		// 获取支付宝GET过来反馈信息
		Map<String, String> params = UtilPay.getpayReturnParamsForVerify(request);
		log.debug("异步回调>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>:" + params);
		boolean verify_result = AlipayNotify.verify(params);
		if (verify_result) {
			log.debug("异步回调验证通过>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			String batch_no = params.get("batch_no");
			String result_details = params.get("result_details");
			String success_num = params.get("success_num");
			Map<String,String> sPra = new HashMap<String,String>();
			sPra.put("batch_no", batch_no);
			sPra.put("success_num", success_num);
			sPra.put("result_details", result_details);
			MemberAlipayRefundLog refundLog = iMemberRefundService.searchByBatchNo(batch_no);
			if (refundLog != null) {
				String url = refundLog.getNotifyUrl();
				// 进行回调
				log.debug("通知状态>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				String result = UtilPay.sendPostInfo(sPra, url, AlipayUtil.key);
				log.debug("通知结果>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+result);
				if (result.equals("success")) {
					// 如果回调成功则更改数据未已退款
					refundLog.setState(MemberAlipayRefundLog.REFUND_SUCCESS_TYPE);
					iMemberRefundService.update(refundLog);
				}
				mav.getModel().put("result", result);
			}
		} else {
			log.debug("异步回调验证失败>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			mav.getModel().put("result", "");
		}
		mav.setViewName("notifyurl");
		return mav;
	}
	/**
	 * CheckDetailData:(监测用户提交退款信息).
	 * @author LiuJiangTao
	 * @param detaildata
	 */
	public boolean CheckDetailData(String detaildata,long memberId) {
		if (StringUtils.isBlank(detaildata))
			return false;
		String[] details = detaildata.split("\\#");
		for (String detail : details) {
			String[] order = detail.split("\\^");
			String tradNo = order[0];
			MemberTradInfo tradInfo = iMemberTradInfoService.getTradInfoByOrderNo(null, tradNo);
			if (tradInfo == null)
				return false;
			if(tradInfo.getMemberId() != memberId) 
				return false;
		}
		return true;
	}

	/**
	 * getSubNumber:(使用正则表达式，返回des字串中的reg字串的个数).
	 * @author LiuJiangTao
	 * @param des 被查询字段
	 * @param reg 查询字符串
	 * @return
	 */
	public int getSubNumber(String des, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(des);
		int count = 0;// 记录个数
		while (m.find()) {
			count++;
		}
		return count;
	}
}
