$(function(){
	var domain=getHost();
	document.domain=domain;
});
//获得域名
var getHost = function(url) {
            var host = "null";
            if (typeof url == "undefined"
                    || null == url)
                url = window.location.href;
            var regex = /.*\:\/\/([^\/|:]*).*/;
            var match = url.match(regex);
            if (typeof match != "undefined"
                    && null != match) {
                host = match[1];
            }
            if (typeof host != "undefined"
                    && null != host) {
                var strAry = host.split(".");
                if (strAry.length > 1) {
                    host = strAry[strAry.length - 2] + "." + strAry[strAry.length - 1];
                }
            }
            return host;
        };