<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="java.util.Calendar"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN" xml:lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<title>库币支付页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=staticPath%>menu/css/base.css" type="text/css" rel="stylesheet" />
<link href="<%=staticPath%>menu/css/layout.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/js/plugin/validation/style.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/css/pay/pay.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="<%=staticPath%>menu/css/top_v3.0.css"/>
<style type="text/css">
	.ImgNull{width:110px;height:110px;margin:15px 30px;+padding-bottom:15px;+background:none;}
	.orderName{font-size:25px;color:#444444;font-family:'Microsoft YaHei'}
</style>
</head>
<body  class="mt30">
	<div class="g-hd hd-new">
		<a href="javascript:void(0);" class="logo"></a>
		<div class="br"></div>
		<span class="tips" style="top:41px;+line-height:30px;+top:34px">库币支付</span>
	</div>
	<form action="${ctx}/coinPay/immediatelyCoinPay" id="payCoinForm" name="payCoinForm" method="post">
		<input type="hidden" id="proPath" value="<%=menuMemberPath%><%=memberWebPath%>/member/getSecurityInformation" />
		<input type="hidden" name="id" value="${id}" />
		<input type="hidden" name="detail" value="${detail}" /> 
		<input type="hidden" name="detailMode" value="${detailMode}" />
		<input type="hidden" name="detailType" value="${detailType}" /> 
		<input type="hidden" name="sign" value="${sign}"/>
		<div class="mt20" id="payInfo">
			<div class="pay404 hauto clearfix">
				<div class="kubi-num fr">
					<p>
						<span>
							<fmt:formatNumber maxFractionDigits="2" value="${tradInfo.productMoney}" />
						</span>
						库币
					</p>
				</div>
				<c:if test="${not empty tradInfo.productUrl}">
					<div class="orderImg fl">
						<img src="${tradInfo.productUrl}" width="110" height="110"/>
					</div>
				</c:if>
				<c:if test="${empty tradInfo.productUrl}">
					<div class="ImgNull fl"></div>
				</c:if>
				<div class="fl payContent">
					<p class="marOrder mb30">
						订单号：<span>${tradInfo.orderNum}</span>
					</p>
					<p>
						订单名称：<span class="orderName">${tradInfo.productName}</span>
					</p>
					<div class="order-scal">
						<p class="mt30">
							订单详细：<span>${tradInfo.productDescription}</span>
						</p>
						<p class="mt20">用户名：${username}</p>
						<p class="mt30">
							交易金额：
							<fmt:formatNumber maxFractionDigits="2" value="${tradInfo.productMoney}" />
							库币
						</p>
						<p class="mt30">
							交易时间：
							<fmt:formatDate value="${tradInfo.addTime}" pattern="yyyy年MM月dd日" />
						</p>
						<p class="mt30 mb20">
							请您在提交信息当日<i>24</i>点前完成支付，否则订单会自动取消
						</p>
					</div>
				</div>
				<p class="orderdetail">
					<a href="javascript:void(0);">订单详情</a>
				</p>
			</div>
			<div class="kubiNum PurService mb70">
				<!--余额充足 begin-->
				<c:if test="${balancelack == true}">
					<p class="pt25" id="Balance">
						库币余额：<b><fmt:formatNumber maxFractionDigits="2" value="${coinBalance}" /></b>
					</p>
				</c:if>
				<!--余额充足 end-->
				<!--余额不足 begin-->
				<c:if test="${balancelack == false}">
					<p class="pt25" id="eBalance">
						库币余额：<fmt:formatNumber maxFractionDigits="2" value="${coinBalance}" />
						<img src="<%=staticPath%>menu/images/warn-b.png" width="12" height="15" alt="" class="kbImg" />
						当前库币余额不足，请<a href="<%=menuMemberPath%><%=memberWebPath%>/coinAlipay/index" target="_blank" title="点击充值" class="bd-yj">充值</a>
					</p>
				</c:if>
				<!--余额不足 end-->
				<!--支付密码 begin-->
				<c:if test="${checkePayPassword == false}">
					<p class="pt35">
						支付密码：<a href="javascript:void(0);" class="ablue" id="setbtn">设置支付密码</a>
					</p>
				</c:if>
				<!--支付密码 end-->
				<!--设置支付密码 begin-->
				<c:if test="${checkePayPassword == true}">
					<p class="pt35 clearfix" >
						<span class="fl pt5">支付密码：</span>
						<input type="password" class="yj_tex_a mr10 fl" id="payPassword" name="payPassword"/>
						<a href="<%=menuMemberPath%><%=memberWebPath%>/member/getSecurityInformation" target="_blank" title="点击找回密码" class="ablue f1 pt5" style="display:inline-block;+display:inline;zoom:1">忘记密码</a>
						<span id="ererr" class ="Validform_checktip mt5 fl"></span>
					</p>
				</c:if>
				<!--设置支付密码 end-->
				<p class="mt30">
					<c:if test="${balancelack == true && checkePayPassword == true}">
						<a href="javascript:void(0);" id="payCoin_Submit" class="yj_w119" title="立即支付">立即支付</a>
					</c:if>
					<c:if test="${balancelack == false || checkePayPassword == false}">
						<a href="javascript:void(0);" class="yj_w119" style="background:#eee;border:1px solid #dfdfdf;color:#fff;" title="立即支付" >立即支付</a>
					</c:if>
				</p>
			</div>
		</div>
	</form>
	<div class="tk-mark disNo"></div>
	<!--遮罩-->
	<div class="order_pop w320-tk orderTk">
		<!--弹框-->
		<h2 class="order_pop_tit">
			<span class="fl">设置</span> <a href="javascript:void(0);" id="colse"></a>
		</h2>
		<div class="order_popcon">
			<div class="message-box hauto kb-pay">
				<p>设置成功前请不要关闭此窗口</p>
				<p>设置成功后，可选择继续支付</p>
			</div>
			<p class="pb10 textC">
				<input type="button" class="confirmBtn w80Btn" id="continuePay" value="继续支付"/>
			</p>
		</div>
	</div>
	<div class="footerN bottom0">
		<p style="text-align: center; font: 100 14px/50px &quot;Microsoft YaHei&quot;;">
			© 1999-<%=Calendar.getInstance().get(Calendar.YEAR)%> 中国网库 All rights reserved.
		</p>
	</div>
	<script type="text/javascript" src="${ctx}/static/js/plugin/validation/Validform_v5.3.2_min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/domain.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/pay/coinPay.js"></script>
	<script src="<%=staticPath%>menu/js/jquery.topbar.js"></script>
</body>
</html>