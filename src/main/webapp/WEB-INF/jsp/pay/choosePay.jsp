<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN" xml:lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>选择支付方式</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=staticPath%>menu/css/base.css" type="text/css" rel="stylesheet" />
<link href="<%=staticPath%>menu/css/layout.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/pay/pay.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="<%=staticPath%>menu/css/top_v3.0.css"/>
</head>
<body  class="mt30">
	<form method="post" id="payForm" action="${ctx}/submitPay">
		<input type="hidden" name="id" value="${id}" />
		<input type="hidden" name="detail" value="${detail}" /> 
		<input type="hidden" name="detailMode" value="${detailMode}" />
		<input type="hidden" name="detailType" value="${detailType}" />
		<input type="hidden" name="orderNum" value="${orderNum}" /> 
		<input type="hidden" name="productName" value="${productName}" /> 
		<input type="hidden" name="productMoney" value="${productMoney}" />
		<input type="hidden" name="sign" value="${sign}" />
		<div class="g-hd hd-new">
			<a href="javascript:void(0);" class="logo"></a>
			<div class="br"></div>
			<span class="tips" style="top: 41px; + line-height: 30px; + top: 34px">选择支付</span>
		</div>
		<div class="w1000 mt20">
			<div class="pay404 payOrder">
				<p class="marOrder mb30">
					订单号：<span>${orderNum}</span>
				</p>
				<p>
					订单名称：<span>${productName}</span>
				</p>
			</div>
			<div class="con_div">
				<ul class="calc_list pl45">
					<li>
						<span>
							<input type="radio" name="kb-input" class="mr11 kpInput" value="2" checked="checked" />
							<img src="<%=staticPath%>menu/images/kb-pay.png" width="100" height="26" alt="" class="mr25" />
							（<i><fmt:formatNumber maxFractionDigits="2" value="${productMoney}" /></i> 库币)
						</span> 
						<span class="ml53">
							<input type="radio" name="kb-input" value="1" class="kpInput" />
							<img src="<%=staticPath%>menu/images/gs_zf.gif" width="110" height="44" alt="" class="mr25" />
							（<i><fmt:formatNumber maxFractionDigits="2" value="${productMoney}" /></i>元）
						</span>
					</li>
				</ul>
				<p class="pl60 mt35 pb35">
					<a href="javascript:void(0);" id="goPay" class="yj_w119" title="下一步">下一步</a>
				</p>
			</div>
			<div class="pay-problem mb30">
				<h3>常见问题</h3>
				<p class="title">1.什么是库币？</p>
				<p>库币是网库平台的一种虚拟的货币，没有有效期，也不可以抵现，不能提现。</p>
				<p class="title">2.怎么获得库币？</p>
				<p>可以通过完成会员中心的各种任务，获得库币；也可以参与平台不定期举办的各种活动，赢得库币；</p>
				<p class="mt12">可以参与平台不定期举办的各种活动，赢得库币；</p>
				<p class="mt12">可以通过充值，得到库币。</p>
				<p class="title">3.库币可以用来干什么？</p>
				<p>订购广告位，让你的商品在平台中更先被搜索到，更容易被别人发现；</p>
				<p class="mt12">购买单品通会员，享受平台的更优质的服务；</p>
				<p class="mt12">去库币商城兑换礼品，更多更好玩的商品等你拿。</p>
				<p class="title">4.库币最少可以充值多少？</p>
				<p>库币单次充值金额是>=1元的正整数，当前库币和人民币的比例是1:1。</p>
				<p class="title">5.通过什么方式能充值库币？</p>
				<p>可以通过支付宝的方式充值库币。</p>
			</div>
		</div>
	</form>
	<script src="<%=staticPath%>menu/js/jquery.topbar.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/pay/choosePay.js"></script>
</body>
</html>