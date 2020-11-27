// 게시글 번호 저장
var boardNo = document.getElementById("boardNo").value;

// 댓글 목록 보기
function commentList() {
	$.ajax({
		url:	"/cbcomment/list/" + boardNo,
		type:	"get",
		data:	{"boardNo": boardNo},
		success: function(data) {
			var str = '';
			$.each(data, function(key, value){ 
				if (key == 0) {
					str += '<div class="col-sm-12" style="padding-top: 10px;">';
				} else {
					str += '<div class="col-sm-12" style="border-top: 1px solid #dddddd; margin-top: 15px; padding-top: 15px;">';
				}
				str += '<div class="col-sm-2">' + value.writerName + ' (' + value.writer + ')' + '</div>';
				str += '<div class="col-sm-8 commentContent' + value.commentNo + '" align="left"><p>' + value.content +'</p></div>';
				str += '<div class="col-sm-2">';
				str += '<button class="btn btn-sm btn-default" onclick="commentUpdate(' + value.commentNo + ',\'' + value.content + '\');">수정</button>&nbsp;';
				str += '<button class="btn btn-sm btn-default" onclick="commentDelete(' + value.commentNo + ');">삭제</button>';
				str += '</div></div>';
			});
			$("#commentList").html(str);
		}
	})
}

// 댓글 작성
function commentInsert(writer, content){
	$.ajax({
		url: 	"/cbcomment/insert",
		type: 	"post",
		data: 	{"writer": writer, "content": content, "boardNo": boardNo},
		success: function(data){
			if(data == 1) {
				commentList(); // 댓글 목록 새로고침
				$("#content").val(""); // 댓글 작성칸 비우기
			}
		}
	});
}