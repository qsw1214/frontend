//钉钉扫码登录
var url = encodeURIComponent('http://localhost:8080/login');
var demo = encodeURIComponent('https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=dingoasfpvwzzv8ntiprfq&response_type=code&scope=snsapi_login&state=STATE&redirect_uri='+url);
var obj = DDLogin({
	id:"login_container",
	goto: demo,
	style: "border:none;background-color:#F1F3F4;",
	width : "365px",
	height: "400px"
});
var hanndleMessage = function (event) {
	var origin = event.origin;
	console.log("origin", event.origin);
	if( origin == "https://login.dingtalk.com" ) { //判断是否来自ddLogin扫码事件。
		var loginTmpCode = event.data; //拿到loginTmpCode后就可以在这里构造跳转链接进行跳转了
		console.log("loginTmpCode", loginTmpCode);
		var url="https://oapi.dingtalk.com/connect/oauth2/sns_authorize?appid=dingoasfpvwzzv8ntiprfq&response_type=code&scope=snsapi_login&state=STATE&redirect_uri=REDIRECT_URI&loginTmpCode="+loginTmpCode;
		location.href=url;   //跳转到指定的页面
	} };
if (typeof window.addEventListener != 'undefined') {
	window.addEventListener('message', hanndleMessage, false);
} else if (typeof window.attachEvent != 'undefined') {
	window.attachEvent('onmessage', hanndleMessage);
}
//2.5.这儿主要是获取code，返回钉钉id，进行后端的钉钉id比对,api接口见下
var url = window.location.search.split("&");
var code=url[0].substring(url[0].lastIndexOf("=")+1);
var status=url[1].substring(url[1].lastIndexOf("=")+1);
if(code!=""&&status=="STATE"){
	var uri="/"
	$.ajax({
		url:"http://localhost:8080/login",
		method:"post",
		data: {
			"code": code,
			"status": status,
			"ways": "dingTalk"
		},
		dataType: "json",
		headers : {'Content-Type' : 'application/x-www-form-urlencoded'}
	}).success(function(data){
		if (data.code === 200) {
			location.href =   '/index';
		} else {
			//提示
			alert(data.code);
			// $MB.n_warning(r.msg);alert()
			//location.href.back();
		}

	});
};

// click
// click
function btn(){

		var username=$('#username').val().trim();
		var pwd=$('#pwd').val().trim();
	    // alert("注册成功");
		$.ajax({
			url:"http://localhost:8080/login",
			method:'post',
			data: {
				"username":username,
				"password":pwd,
			},
			datatype:"json",
			success: function(data){
				window.location.href= '/index';
			},
			error: function(data){
				alert('登陆失败')
				window.location.back();

			}
		});

};