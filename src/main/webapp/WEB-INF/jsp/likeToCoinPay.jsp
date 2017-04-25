<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" dir="ltr" lang="zh-CN"
	xml:lang="zh-CN">
<head>
<%@ include file="/common/global.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<link href="<%=staticPath%>menu/css/base.css" type="text/css" rel="stylesheet" />
<link href="<%=staticPath%>menu/css/layout.css" type="text/css" rel="stylesheet" />
<link href="${ctx}/static/js/plugin/validation/style.css" type="text/css" rel="stylesheet" />
<link type="text/css" rel="stylesheet" href="<%=staticPath%>menu/css/top_v3.0.css"/>
<style type="text/css">
g-hd .tips {
	top: 41px; +
	line-height: 37px; +
	top: 31px
}

.kubi-num p span { +
	vertical-align: middle
}

.kubi-num { +
	padding-bottom: 9px
}

.iniframe {
	width: 100%;
}
</style>
<title>库币支付页面</title>
</head>
<body class="mt30">
	<form action="${ctx}/memberCoin/immediatelyCoinPay" id="payCoinForm"
		name="payCoinForm" method="post">
		<input type="hidden" id="checkePayPassword" name="checkePayPassword" value="${checkePayPassword}" /> 
		<input type="hidden" id="proPath" value="<%=menuMemberPath%><%=memberWebPath%>/member/getSecurityInformation" />
		<input type="hidden" id="tradId" name="tradId" value="${tradInfo.id}" />
		<input type="hidden" id="detail" name="detail" value="${detail}" /> 
		<input type="hidden" id="detailMode" name="detailMode" value="${detailMode}" />
		<input type="hidden" id="detailType" name="detailType" value="${detailType}" /> 
		<input type="hidden" id="productMoney" name="productMoney" value="${tradInfo.productMoney }" /> 
		<input type="hidden" id="coinBalance" name="coinBalance" value="${coinBalance}" />
		<input type="hidden" id="tradFlag" name="tradFlag" value="${tradInfo.tradFlag }"/>
		<div class="g-hd hd-new">
			<a href="#" class="logo"></a>
			<div class="br"></div>
			<span class="tips" style="top:41px;+line-height:30px;+top:34px">库币支付</span>
		</div>
		<div class="mt20" id="payInfo">
			<div class="pay404 hauto clearfix">
				<div class="kubi-num fr">
					<p>
						<span><fmt:formatNumber maxFractionDigits="2"
								value="${tradInfo.productMoney }" /> </span>库币
					</p>
				</div>
				<div class="orderImg fl">
					<img src="${tradInfo.productUrl}" width="110" height="110" alt="" />
				</div>
				<div class="fl payContent">
					<p class="marOrder mb30">
						订单号：<span>${tradInfo.orderNum}</span>
					</p>
					<p>
						订单名称：<span>${tradInfo.productName}</span>
					</p>
					<div class="order-scal">
						<p class="mt30">
							订单详细：<span>${tradInfo.productDescription}</span>
						</p>
						<p class="mt20">用户名：${username}</p>
						<p class="mt30">
							交易金额：
							<fmt:formatNumber maxFractionDigits="2"
								value="${tradInfo.productMoney }" />
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
					<a href="#">订单详情</a>
				</p>
			</div>
			<div class="kubiNum PurService mb70">
				<!--余额充足 begin-->
				<p class="pt25" id="Balance">
					库币余额：<b><fmt:formatNumber maxFractionDigits="2" value="${coinBalance}" /></b>
				</p>
				<!--余额充足 end-->
				<!--余额不足 begin-->
				<p class="pt25" style="display: none;" id="eBalance">
					库币余额：<fmt:formatNumber maxFractionDigits="2" value="${coinBalance}" />
					<img src="<%=staticPath%>menu/images/warn-b.png" width="12"
						height="15" alt="" class="kbImg" />当前库币余额不足，请<a
						href="<%=menuMemberPath%><%=memberWebPath%>/coinAlipay/index"
						target="_blank" title="" class="bd-yj">充值</a>
				</p>
				<!--余额不足 end-->
				<!--支付密码 begin-->
				<c:if test="${checkePayPassword == false}">
					<p class="pt35">
						支付密码：<a href="#" title="" class="ablue" id="setbtn">设置支付密码</a>
					</p>
				</c:if>
				<!--支付密码 end-->
				<!--设置支付密码 begin-->
				<c:if test="${checkePayPassword == true}">
					<p class="pt35 clearfix" ><!-- style="width:552px;" -->
						<span class="fl pt5">支付密码：</span><input type="password" class="yj_tex_a mr10 fl" id="payPassword"
							name="payPassword"  <%-- maxlength="16" datatype="*6-16"
							nullmsg="请输入支付密码！" errormsg="密码范围在6~16位之间！"
							ajaxurl="${ctx}/memberCoin/isCheckingPayPassword" --%> />
							<a href="<%=menuMemberPath%><%=memberWebPath%>/member/getSecurityInformation"
							target="_blank" title="" class="ablue f1 pt5" style="display:inline-block;+display:inline;zoom:1">忘记密码</a>
							<span id="ererr"></span>
					
					</p>
				</c:if>
				<!--设置支付密码 end-->
				<p class="mt30">
					<a href="javascript:;" id="payCoin_Submit" class="yj_w119"
						title="立即支付">立即支付</a>
				</p>
			</div>
		</div>
		<div class="tk-mark disNo"></div>
		<!--遮罩-->
		<div class="order_pop w320-tk orderTk">
			<!--弹框-->
			<h2 class="order_pop_tit">
				<span class="fl">设置</span> <a href="#nogo" id="colse"></a>
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
		<div class="footerN bottom0"></div>
	</form>
<%-- 	<script type="text/javascript" src="<%=staticPath%>menu/js/jquery-1.7.min.js"></script> --%>
	<script type="text/javascript" language="javascript" src="${ctx}/static/js/plugin/validation/Validform_v5.3.2_min.js"></script>
	<script type="text/javascript" src="${ctx}/static/js/domain.js"></script>
    <script src="<%=staticPath%>menu/js/jquery.topbar.js"></script>
	<script type="text/javascript">
	$(function() {
		var element = self.frameElement;
		if(element == null || element.tagName!="IFRAME"){
			$('#payInfo').addClass('w1000');
		}else{
			$('#payInfo').addClass('iniframe');
		}
		var productMoney = parseInt($("#productMoney").val());
		var coinBalance = parseInt($("#coinBalance").val());
		if (productMoney > coinBalance) {
			$("#eBalance").show();
			$("#Balance").hide();
		}
		$("#payCoin_Submit").on('click',function(){
			if (productMoney > coinBalance) {
				return false;
			}
			var flag = $("#checkePayPassword").val();
			if (flag == "false") {
				alert("请设置支付密码！！！！");
				return false;
			}
			var ajaxPwd=ajaxPwds();
			if(ajaxPwd==false){
				return false;
			}
			var ererr=$("#ererr");
			var payPassword=$("#payPassword").val();
			$.post('${ctx}/memberCoin/isCheckingPayPassword',{param:payPassword},function(data){
				 if(data){
					 ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_right fl").text("");
					 $("#payCoinForm").submit();
				 }else{
					 ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_wrong fl").text("支付密码错误！！");
					 return false;
				 }
				
			}); 
		});
		
		//$('.tk-mark').hide();
		$('.orderTk').hide();
		$('.order-scal').hide();
		$('.orderdetail').on('click', function() {
			$('.order-scal').slideToggle();
/* 			$('.order-scal').slideToggle(function(){
				if ($(this).is(':hidden')) {
					$('.footerN').addClass('bottom0').removeClass('mt30');
				}else{
					$('.footerN').removeClass('bottom0').addClass('mt30');
				}
			}); */
		});
		$('#setbtn').click(function() {
			var proPath = $("#proPath").val();
			window.open(proPath);
			$('.tk-mark').show();
			$('.orderTk').show();
		});
		$("#colse").on('click', function() {
			$('.tk-mark').hide();
			$('.orderTk').hide();
		});
		$("#continuePay").on('click', function() {
			window.location.reload();
		});
		
	});
	
	$("#payPassword").blur(function(){
		var ajaxPwd=ajaxPwds();
		if(ajaxPwd==false){
			return false;
		}
		var payPassword=$("#payPassword").val();
		var ererr=$("#ererr");
		$.post('${ctx}/memberCoin/isCheckingPayPassword',{param:payPassword},function(data){
			 if(data){
				 ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_right fl").text("");
				 return true;
			 }else{
				 ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_wrong fl").text("支付密码错误！！");
				 return false;
			 }
			
		}); 
	});
	function ajaxPwds(){
		var payPassword=$("#payPassword").val();
		var ererr=$("#ererr");
		if(payPassword=="" || payPassword==null){
			ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_wrong fl").text("支付密码不能为空！");
			return false;
		}
		var re=/^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,16}$/;
		if(!re.test(payPassword)){
			ererr.attr("class","Validform_checktip fl ml10 mt5 Validform_wrong fl").text("支付密码6-16字符之间！！");
			return false;
		}
		return true;
	}
	
</script>
</body>
</html>