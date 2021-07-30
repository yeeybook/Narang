package com.exp.narang.webrtc.response;

import com.exp.narang.common.model.response.BaseResponseBody;
import io.openvidu.java.client.OpenViduRole;
import io.openvidu.java.client.Session;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@ApiModel("UserResponse")
public class SessionTokenRes extends BaseResponseBody {
    @ApiModelProperty(name = "세션의 사용자 구분을 위한 토큰", example = "owqhepgo3wirg04liuho...")
    String token;
    Session session;
    Map<String, OpenViduRole> map;
    public static SessionTokenRes of(Integer statusCode, String message, String token ,Session session, Map<String, OpenViduRole> map){
        SessionTokenRes res = new SessionTokenRes();
        res.setStatusCode(statusCode);
        res.setMessage(message);
        res.setSession(session);
        res.setToken(token);
        res.setMap(map);
        return res;
    }
}
