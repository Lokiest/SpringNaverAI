<%@ page contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Naver Cloud AI Service  
</h1>

<P>   
	<ul>
		<li><a href="summaryform">Clova Summary: 긴 문장을 요약해주는 API</a></li>
		<li><a href="cfrform">CFR-얼굴감지 (Celebrity)셀럽 얼굴 감지 </a></li>
		<li><a href="csrform">CSR-음성을 감지하여 텍스트로 변환</a></li>
	</ul>

</P>
<textarea id="text" name="text"></textarea>
<button id="speakerBtn">음성확인</button>

<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
<script>
$(function(){
	$('#speakerBtn').click(()=>{
		alert($('#text').val());
		//비동기식 처리
		let url="voiceOk";
		/*$.ajax({
			type:'post',
			url:url,
			data:{text:$('#text').val()},
			dataType:'blob',
			contentType:'application/x-www-form-urlencoded; charset=UTF-8',
			success:function(res){
				alert("success: "+res);
				var audioUrl=URL.createObjectURL(res);
				//받은 데이터를 음성으로 들려줘야 한다.
				var audio = new Audio()
				audio.src=audioUrl				
				audio.play();
			},
			error:function(err){
				alert('error: '+err.status);
			}
		})*/
		let xhr=new XMLHttpRequest()
		
		//응답받을 데이터의 데이터타입 설정
		xhr.responseType='blob';//binary code로 응답온다는 얘기
		//서버에서 응답받으면 실행할 콜백함수
		xhr.onload=function(response){
			console.log(response)
			var audioUrl=createObjectURL(response)//URL.createObjectURL(response);
			//받은 데이터를 음성으로 들려줘야 한다.
			var audio = new Audio()
			audio.src=audioUrl				
			audio.play();
		}
		xhr.open('post',url,true)
		xhr.setRequestHeader('Content-Type','application/x-www-form-urlencoded; charset=UTF-8')
		xhr.send("text="+$('#text').val())
		
					
	})
	function createObjectURL(object) {
    		return (window.URL) ? window.URL.createObjectURL(object) : window.webkitURL.createObjectURL(object);
		}
})
</script>
</body>
</html>
