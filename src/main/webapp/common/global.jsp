<%@include file="globalUrl.jsp"  %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<link rel="stylesheet" type="text/css" href="<%=staticPath %>js/common/plugins/custom/paginate/paginate.css" />
<link rel=stylesheet type=text/css href="<%=memberCssPath %>base.css"> 
<link rel=stylesheet type=text/css href="<%=memberCssPath %>wbox.css">
<script type="text/javascript" src="<%=staticPath %>js/common/core/jquery.js"></script>
<script type="text/javascript" src="<%=staticPath %>js/common/plugins/custom/paginate/jquery.paginate.js"></script>
<script type="text/javascript" src="<%=staticPath %>admin/js/date.js"></script>
<script type="text/javascript" src="<%=staticPath %>admin/js/ui.js"></script>
<input type="hidden" value="${pageContext.request.contextPath}" id="ctx" />
