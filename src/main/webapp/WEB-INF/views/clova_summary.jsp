<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>퇴실찍자 퇴실 제발</title>
<script src="https://cdn.jsdelivr.net/npm/jquery@3.6.1/dist/jquery.min.js"></script>
<script>
	$(function() {
		$('#btnSummary').click(function() {
			let title = $('#title').val();
			let content = $('#content').val();
			if(!title||!content) {
				alert('insert title and content');
				return;
			}
			
			let url = "summaryEnd";
			$.ajax({
				type:'post',
				url:url,
				dataType:'text',
				data:{
					title:title,
					content:content
				},
				success:function(res) {
					$('#result').html("<h4>" + res + "</h4>");
				},
				error:function(err) {
					alert('err : ' + err.status);
				}
			})
			
		})
	})
</script>
</head>
<body>
<div id="wrap">
	<h1>Naver Clova Summary</h1>
	title : <input type="text" id="title" name="title" style="width:70%;">
	<br><br>
	content : <textarea rows="20" cols="100" name="content" id="content"></textarea>
	<button id="btnSummary">sentence summary</button>
	<hr color='red'>
	<div id="result"></div>
</div>
</body>
</html>