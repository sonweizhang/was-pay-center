	$(function() {
		//iframe嵌入自适应
		var element = self.frameElement;
		if (element == null || element.tagName != "IFRAME") {
			$('#payInfo').addClass('w1000');
		} else {
			$('#payInfo').addClass('iniframe');
		}
		
		// 绑定用户提交事件
		$("#payCoin_Submit").on('click',function(){
			checkPassword(toSubmit);
		});
		
		$('.orderTk').hide();
		$('.order-scal').hide();
		$('.orderdetail').on('click', function() {
			$('.order-scal').slideToggle();
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
		
		// 重新支付
		$("#continuePay").on('click', function() {
			window.location.reload();
		});
		
		$("#payPassword").blur(function(){
			checkPassword();
		});
	});
	
	/**
	 * 监测支付密码是否正确
	 * @returns {Boolean}
	 */
	function checkPassword(callback){
		var payPassword=$("#payPassword").val();
		var ctx = $('#ctx').val();
		$("#ererr").removeClass('Validform_right');
		var re=/^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,16}$/;
		if (!re.test(payPassword)) {
			$("#ererr").addClass('Validform_wrong').text("支付密码6-16字符之间！！");
			return false;
		}
		$.post(ctx+'/checkPayPassword',{param:payPassword},function(data){
			 if(data){
				 $("#ererr").removeClass('Validform_wrong');
				 $("#ererr").addClass('Validform_right').text("");
				 if(callback) callback();
			 }else{
				 $("#ererr").removeClass('Validform_right');
				 $("#ererr").addClass('Validform_wrong').text("支付密码错误！！");
			 }
		}); 
	}
	
	function toSubmit(){
		$("#payCoinForm").submit();
	}
	
