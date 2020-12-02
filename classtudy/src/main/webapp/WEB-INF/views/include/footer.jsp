<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- JQuery & Bootstrap -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
<!-- Classtudy Function -->
<script src="/static/js/csfunction.js"></script>
<!-- Markdown Editor & View -->
<script src="/static/js/editormd.js"></script>
<script src="/static/js/lib/marked.min.js"></script>
<script src="/static/js/lib/prettify.min.js"></script>
<script>
$(document).ready(function(){
	
	// Multi Dropdown Menu
	$('.dropdown-submenu a.test').on("click", function(e){
		$(this).next('ul').toggle();
		e.stopPropagation();
		e.preventDefault();
	});
	
	$(window).on("load", notiLoad());
	
});

// 새로운 알림이 있는지 확인
function notiLoad() {
	var memberId = document.getElementById("loginId").value;
	$.ajax({
		url:	"/noti/load/",
		type:	"post",
		data:	{"memberId": memberId},
		success: function(data) {
			var notiBadge = '';
			notiBadge += '<a class="dropdown-toggle" data-toggle="dropdown" href="#" onclick="notiList(\'' + memberId + '\')">알림 ';
			if (data > 0) {
				notiBadge += '<span class="badge" style="background-color: #FFB132;">' + data + '</span></a>';
			} else {
				notiBadge += '<span class="badge">' + data + '</span></a>';
			}
			notiBadge += '<ul class="dropdown-menu" id="notiList"></ul>';
			$("#notiBadgeArea").html(notiBadge);
		}
	});
}

// 알림 목록 보기
function notiList(memberId) {
	//alert("notiList 실행");
	$.ajax({
		url:	"/noti/list/" + memberId,
		type:	"post",
		data:	{"memberId": memberId},
		success: function(data) {
			var str = '';
			if (data.length < 1) { //새로운 알림이 없을 때
				str += '<div class="col-sm-12">새로운 알림이 없습니다.</div>';
			} else {
				$.each(data, function(key, value){
					if (key == 0) {
						str += '<div class="col-sm-12">';
					} else {
						str += '<div class="col-sm-12" style="border-top: 1px solid #dddddd; margin-top: 10px; padding-top: 10px;">';
					}
					str += '<div class="col-sm-10" align="left">' + value.content + '</div>';
					str += '<div class="col-sm-2">';
					str += '<button class="btn btn-sm btn-default" onclick="notiCheck(' + value.notiNo + ');">확인</button>';
					str += '</div></div>';
				});
				str += '<div class="col-sm-12" align="center" style="padding-top: 10px;">'
				str += '<button class="btn btn-warning" onclick="notiCheckAll(\'' + memberId + '\');">모두 확인</button>'
				str += '</div>';
			}
			$("#notiList").html(str);
		}
	});
	//alert("notiList 끝");
}

// 알림 확인
function notiCheck(notiNo) {
	$.ajax({
		url: 	"/noti/check/" + notiNo,
		type: 	"post",
		dataType: "json",
		data: 	{"notiNo" : notiNo},
		success: function(data) {
			if(data == 1) { notiLoad(); } //알림 확인 후 알림 뱃지를 다시 출력
		}
	});
}

//알림 모두 확인
function notiCheckAll(memberId) {
	$.ajax({
		url: 	"/noti/checkAll/",
		type: 	"post",
		dataType: "json",
		data: 	{"memberId" : memberId},
		success: function(data) {
			if(data > 0) { notiLoad(); } //알림 확인 후 알림 뱃지를 다시 출력
		}
	});
}
</script>