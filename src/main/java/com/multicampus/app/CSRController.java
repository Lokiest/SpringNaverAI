package com.multicampus.app;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class CSRController {
	String clientId = "0xok1o8ptm"; 
    String clientSecret = "AbECzqJO1yw9jhtOPJ3Bzu48kIB0oEMUdv2RuPmV";
    
	@GetMapping("/csrform")
	public ModelAndView csrform() {
		
		return new ModelAndView("clova_speech");
	}
	@PostMapping(value="/csr_speech", produces = "application/json")
	public Map<String, String> speechRecognition(@RequestParam("mp3file") MultipartFile mfile,
			@RequestParam("language") String language, HttpSession ses) {
		
		String clientId = "0xok1o8ptm"; 
        String clientSecret = "AbECzqJO1yw9jhtOPJ3Bzu48kIB0oEMUdv2RuPmV";
        
		ServletContext app=ses.getServletContext();
		
		String path=app.getRealPath("/file");
		String fileName=mfile.getOriginalFilename();
		File dir=new File(path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		//File upFile=new File(path, fileName);
		//mp3파일 업로드 처리
		//mfile.transferTo(upFile);
		Map<String, String> map=new HashMap<>();
		try {
            String imgFile = path+fileName;//음성파일 경로
            System.out.println(imgFile);
            File voiceFile = new File(path, fileName);
            mfile.transferTo(voiceFile);//업로드 처리
            
            // 언어 코드 ( Kor, Jpn, Eng, Chn )
            String apiURL = "https://naveropenapi.apigw.ntruss.com/recog/v1/stt?lang=" + language;
            URL url = new URL(apiURL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
            conn.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

            OutputStream outputStream = conn.getOutputStream();
            FileInputStream inputStream = new FileInputStream(voiceFile);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            inputStream.close();
            ////////////////////////////////////////////////
            BufferedReader br = null;
            int responseCode = conn.getResponseCode();
            if(responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            } else {  // 오류 발생
                System.out.println("error!!!!!!! responseCode= " + responseCode);
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream(),"UTF-8"));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            if(br != null) {
              
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
                map.put("result", response.toString());
            } else {
                System.out.println("error !!!");
                map.put("result", "error!!!");
            }
            
        } catch (Exception e) {
            System.out.println(e);
            map.put("result", "error!!!: "+e.getMessage());
        }
		
		return map;
	}

	@PostMapping("/voiceOk")
	@ResponseBody
	public void voiceOk(@RequestParam("text") String text, HttpServletResponse res) {
		System.out.println("text=>"+text);
				
	        try {
	            text = URLEncoder.encode(text, "UTF-8"); // 13자
	            String apiURL = "https://naveropenapi.apigw.ntruss.com/tts-premium/v1/tts";
	            URL url = new URL(apiURL);
	            HttpURLConnection con = (HttpURLConnection)url.openConnection();
	            con.setRequestMethod("POST");
	            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
	            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);
	            // post request
	            String postParams = "speaker=noyj&volume=0&speed=0&pitch=0&format=mp3&text=" + text;
	            con.setDoOutput(true);
	            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
	            wr.writeBytes(postParams);
	            wr.flush();
	            wr.close();
	            int responseCode = con.getResponseCode();
	            BufferedReader br;
	            if(responseCode==200) { // 정상 호출
	            	System.out.println("성공:==========");
	                InputStream is = con.getInputStream();
	                int read = 0;
	                byte[] bytes = new byte[1024];
	                // 랜덤한 이름으로 mp3 파일 생성
	                String tempname = Long.valueOf(new Date().getTime()).toString();
	                File f = new File(tempname + ".mp3"); 
	                System.out.println(f.getAbsolutePath());
	                f.createNewFile();
	               // OutputStream outputStream = new FileOutputStream(f);
	                ServletOutputStream outputStream=res.getOutputStream();
	                //우리 서버로 응답을 보내는 작업을 해보자.
	                while ((read =is.read(bytes)) != -1) {
	                    outputStream.write(bytes, 0, read);
	                    outputStream.flush();
	                }
	                is.close();
	                outputStream.close();
	            } else {  // 오류 발생
	            	System.out.println("오류발생:==========");
	                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	                String inputLine;
	                StringBuffer buf = new StringBuffer();
	                while ((inputLine = br.readLine()) != null) {
	                	buf.append(inputLine);
	                }
	                br.close();
	                System.out.println(buf.toString());
	            }
	        } catch (Exception e) {
	            System.out.println(e);
	            e.printStackTrace();
	        }
		
	}//------------------------------------------
}
