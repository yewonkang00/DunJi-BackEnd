package com.dunji.backend.domain.user.application;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Slf4j
public class UserService {

    //access_token 발급
    public String getKakaoAccessToken(String code) {
        String access_Token = "";
        String refresh_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //  URL연결은 입출력에 사용 될 수 있고, POST 혹은 PUT 요청을 하려면 setDoOutput을 true로 설정해야함.
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            //POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=00c48270395b6a27deb3c5a044c1407f"); // REST_API_KEY 입력
            sb.append("&redirect_uri=http://3.39.129.136:8090/DungziProject/login/kakao"); // To-Do 인가코드 받은 redirect_uri 입력
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();

            log.debug("access_token : {}", access_Token);
            log.debug("refresh_token : {}", refresh_Token);


            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return access_Token;
    }

    // access_token을 이용하여 사용자 정보 조회
    public void getUserInfo(String token) throws Exception {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리로 JSON파싱
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject profile = element.getAsJsonObject().get("properties").getAsJsonObject();
            JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            Long id = element.getAsJsonObject().get("id").getAsLong();

            // email, ci, nickname 필수 값으로 변경시 수정 필요
            boolean hasEmail = kakao_account.getAsJsonObject().get("has_email").getAsBoolean();
            String email = "";
            if (hasEmail) {
                email = kakao_account.getAsJsonObject().get("email").getAsString();
            }

            boolean hasNickname = profile.getAsJsonObject().get("has_nickname").getAsBoolean();
            String nickname = "";
            if (hasNickname) {
                nickname = profile.getAsJsonObject().get("nickname").getAsString();
            }

            boolean hasCi = kakao_account.getAsJsonObject().get("has_ci").getAsBoolean();
            String ci = "";
            if (hasCi) {
                ci = kakao_account.getAsJsonObject().get("ci").getAsString();
            }

            log.debug("id : {}", id);
            log.debug("email : {}", email);
            log.debug("nickname : {}", nickname);
            log.debug("ci : {}", ci);

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
