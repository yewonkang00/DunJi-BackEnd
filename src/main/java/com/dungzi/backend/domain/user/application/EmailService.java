package com.dungzi.backend.domain.user.application;

import com.dungzi.backend.global.common.response.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

//@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    private final String EMAIL_TITLE = "이메일 주소 확인";
    private final String EMAIL_TEXT = "아래 확인 코드를 이메일 인증 화면에서 입력해주세요.";

    @Value("${EMAIL_SEND_ADDRESS}")
    private String address;


    /*
        메일 발송
        sendSimpleMessage의 매개변수로 들어온 to는 인증번호를 받을 메일주소
        MimeMessage 객체 안에 내가 전송할 메일의 내용을 담아준다.
        bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일 send
     */
    public String sendSimpleMessage(String to) throws Exception {
        log.info("[SERVICE] sendSimpleMessage");

        //인증번호 생성
        String emailAuthCode = createKey();

        MimeMessage message = createMessage(to, emailAuthCode);
        try{
            javaMailSender.send(message); // 메일 발송
        }catch(AuthException authException){
            authException.printStackTrace();
            throw new IllegalArgumentException();
        }
        return emailAuthCode; // 메일로 보냈던 인증 코드를 서버로 리턴
    }

    private MimeMessage createMessage(String to, String emailAuthCode)throws MessagingException, UnsupportedEncodingException {
//        log.info("보내는 대상 : "+ to);
//        log.info("인증 번호 : " + ePw);
        MimeMessage  message = javaMailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to); // to 보내는 대상
        message.setSubject("[둥지] 이메일 인증 코드입니다."); //메일 제목

        // 메일 내용 메일의 subtype을 html로 지정하여 html문법 사용 가능
        String msg = getHtmlString(emailAuthCode);

        message.setText(msg, "utf-8", "html"); //내용, charset타입, subtype
        message.setFrom(new InternetAddress(address,"둥지")); //보내는 사람의 메일 주소, 보내는 사람 이름

        return message;
    }

    // 인증코드 만들기
    private static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 6; i++) { // 인증코드 6자리
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    //TODO : html 파일 분리 고려
    private String getHtmlString(String emailAuthCode){
        StringBuilder sb = new StringBuilder();
        sb.append("<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">");
        sb.append(EMAIL_TITLE);
        sb.append("</h1>");
        sb.append("<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">");
        sb.append(EMAIL_TEXT);
        sb.append("</p>");
        sb.append("<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">");
        sb.append(emailAuthCode);
        sb.append("</td></tr></tbody></table></div>");
        String msg = sb.toString();

//        String msg="";
//        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">"+EMAIL_TITLE+"</h1>";
//        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">"+EMAIL_TEXT+"</p>";
//        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
//        msg += EMAIL_AUTH_CODE;
//        msg += "</td></tr></tbody></table></div>";

        return msg;
    }
}
