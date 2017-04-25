<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<% 
	String staticWebPath="http://static.99114.com"; //静态工程地址
	String menuWebPath="http://menu.99114.cn"; //菜单工程地址
	String businessWebPath="http://oppor.99114.cn";  //发布商机地址
	String shopWebPath="http://shop.99114.cn"; //商店地址
	String imagesWebPath="http://upload.99114.cn"; //图片库地址
	String viewPurchasePath="http://caigou.99114.cn/";// 查看采购信息
	String viewSupplyPath="http://pifa.99114.cn/";//查看供应信息
    String memberWebPath="http://member.99114.cn"; //会员中心地址
	String singleProductThroughPath="http://plw.99114.cn/hyt/";//行业通
	String tradeThroughPath="http://plw.99114.cn/dpt/";//单品通
	String privatePrice="http://pifa.99114.cn/";//查询私密报价
	String dealPath="http://deal.99114.cn/";//交易
	String coidWebPath = "http://market.99114.cn";//库币商城地址
	String wkWebPaht = "http://www.99114.cn";//主站地址
%>
<%
	String staticPath = staticWebPath + "/static/";
	String cssPath =  staticWebPath + "/static/admin/css/";
	String memberCssPath = staticWebPath + "/static/member/css/";
	String imgPath =  staticWebPath + "/static/admin/images/";
	String menuBuyPath= menuWebPath+"?mType=1&fromUrl=";//买家
	String menuSalePath=menuWebPath+ "?mType=2&fromUrl=";//卖家
	String menuMemberPath=menuWebPath+ "?mType=3&fromUrl=";//卖家
	String memberPath= menuWebPath+"?myType=6&fromUrl=";//会员中心
	String memberApplication= menuWebPath+"?myType=7&fromUrl=";//应用中心
	String menuAgent= menuWebPath+"/view/agent.jsp";//会员中心 
	String fbgyPath=businessWebPath+"/oftenCate/chooseCategory";//发供应
	String guidStyle = menuWebPath + "/view/buy/memberIframe.jsp?flag=";//桌面引导样式
	String messageCenter = menuWebPath + "/view/buy/messagCenter.jsp?flag=";//桌面引导样式
	String ckdpPath=shopWebPath+"/";//查询店铺
	String zxdpPath=shopWebPath+"/design";//装修店铺
	String fbcgPath= businessWebPath + "/purchase/toSave";//发不采购
	String  imagesUrl = imagesWebPath +"/imgalbum/toImgAlbumInterface";//图片库
	String iOpenShop=shopWebPath+"/design/open" ;//我要开店铺URL
	String tempUrl="javascript:void(0);";
	String  agentUrl=menuWebPath+"/view/agentnot.jsp";
	String domain = "99114.cn";
	String creditApplyRegisterUrl = memberWebPath+"/memberCreditApply/index";
%>	
	
<% 
/*      

买卖家 首页 	URL	 */


	//卖家中心  供应 
	String saleSupply=businessWebPath+"/products/productHome";

	// 卖家中心  交易  等待付款
	String saleTradePay=businessWebPath+"/sellerOrders/toSellerOrdersList?payment=online&status=0&currentPage=1&pageSize=10&orderStatus=1";
	// 卖家中心  交易  等待发货
	String saleTradeShip=businessWebPath+"/sellerOrders/toSellerOrdersList?payment=online&status=1&currentPage=1&pageSize=10&orderStatus=2";
	// 卖家中心  交易  买家确认收货
	String saleTradeConfirmReceipt=businessWebPath+"/sellerOrders/toSellerOrdersList?payment=online&status=2&currentPage=1&pageSize=10&orderStatus=3";
	// 卖家中心  交易   等待评价
	String saleTradeAppraisal=businessWebPath+"/sellerOrders/toSellerOrdersList?payment=online&status=3&currentPage=1&pageSize=10";
	
	// 卖家中心  潜在生意  已收询盘
	String saleToQuotedPrice=businessWebPath+"/quote/searchList?type=to&currentPage=1&pageSize=5";
	// 卖家中心  潜在生意  已发出报价
	String saleFromQuotedPrice=businessWebPath+"/propurchasequote/getTransationPager?currentPage=1&pageSize=3&addTimeSort=0";
	//
	String verificationPath=memberWebPath+"/memberCredit/toIndex";
	
	// 卖家中心 　为您推荐  补充供应信息
	String saleAddSupply=memberWebPath+"/memberDetail/getCompanyDetia";
	
	//供应详细信息  地址
	String  saleSupplyDetail=shopWebPath+"/browse/product/detail";
	//报价详细信息  地址
	String  saleQuotedPriceDetail=businessWebPath+"/propurchasequote/getTransationPager?currentPage=1&pageSize=3&addTimeSort=0";
	//信用认证
	String rzUrl=memberWebPath+"/memberCredit/toRecord?memberId=";
	//购买详细信息 地址
	String  salePurchaseDetail="javascript:void(0);";
	
	
    /**
            买家首页地址信息
    */
 
	//询盘详细信息 地址
	String  buyViewPlateDetail=businessWebPath+"/quote/searchList?currentPage=1&pageSize=5";
	//采购详细信息  地址
	String  buyPurchaseDetail="javascript:void(0);";
	
	//供应公司详细信息 地址
	String  buySupplyFirmDetail=shopWebPath+"/browse/home";
	
	//供应详细信息  地址
	String  buySupplyDetail=shopWebPath+"/browse/product/detail";
	
	
	
	// 买家中心  采购 
	String buyPurchase=businessWebPath+"/purchase/getPager?currentPage=1&pageSize=10&status=2";

	
	// 买家中心  交易  等待付款
	String buyTradePay=businessWebPath+"/buyerOrders/toBuyerOrdersList?status=0&currentPage=1&pageSize=10&orderStatus=1";
	// 买家中心  交易  确认收货
	String buyTradeReceipt=businessWebPath+"/buyerOrders/toBuyerOrdersList?status=2&currentPage=1&pageSize=10&orderStatus=3";
	// 买家中心  交易  等待卖家发货
	String buyTradeConfirmOrder=businessWebPath+"/buyerOrders/toBuyerOrdersList?status=1&currentPage=1&pageSize=10&orderStatus=2";
	// 买家中心  交易   等待评价
	String buyTradeAppraisal=businessWebPath+"/buyerOrders/toBuyerOrdersList?status=3&currentPage=1&pageSize=10";
	// 买家中心  潜在生意  已收到报价
	String buyToQuotedPrice=businessWebPath+"/purchase/getPager?currentPage=1&pageSize=10&status=2";

	// 买家中心  潜在生意  已发出寻盘
	String buyFromQuotedPrice=businessWebPath+"/quote/searchList?currentPage=1&pageSize=5";
		
	// 买家中心 　为您推荐  补充采购信息
	String buyAddPurchase=memberWebPath+"/memberDetail/getCompanyDetial";	
%>	
<%  
String defaultImageUrl=""; // 默认图片地址


%>
	
	



