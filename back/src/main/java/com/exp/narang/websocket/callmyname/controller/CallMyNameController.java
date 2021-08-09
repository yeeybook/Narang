package com.exp.narang.websocket.callmyname.controller;

import com.exp.narang.api.model.service.RoomService;
import com.exp.narang.websocket.callmyname.model.manager.GameManager;
import com.exp.narang.websocket.callmyname.request.NameReq;
import com.exp.narang.websocket.callmyname.response.GuessNameRes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@Slf4j
@AllArgsConstructor
public class CallMyNameController {
    private static class ManagerHolder{
        private static final Map<Long, GameManager> gameManagerMap = new ConcurrentHashMap<>();
    }

    private final RoomService roomService;

    /**
     * 게임을 시작하는 메서드
     * @param roomId : path로 받는 roomId (PK)
     * @return 처음 이름을 정할 사용자의 userID (PK)
     */
    @MessageMapping("/call/start/{roomId}")
    @SendTo("/from/call/start/{roomtId}")
    public long startGame(@DestinationVariable long roomId){
        return ManagerHolder.gameManagerMap
                .put(roomId, new GameManager(roomId, roomService))
                .getFirstUserId();
    }

    /**
     * 정해진 이름을 저장하는 메서드
     * TODO : 순서대로 되는 로직인지 확인하기
     * @param roomId : path로 받는 roomId (PK)
     * @param req : userId와 정해진 이름을 멤버변수로 가진 객체
     * @return 다음으로 이름을 정할 사용자의 userId, 이름 다 정하면 -1
     */
    @MessageMapping("/call/set-name/{roomId}")
    @SendTo("/from/call/set-name/{roomId}")
    public long setName(@DestinationVariable long roomId, NameReq req){
        return ManagerHolder.gameManagerMap.get(roomId).setName(req);
    }

    /**
     * 사용자가 자신의 이름을 맞힐 때 호출되는 메서드
     * TODO : 반환값 필요한거 추가하기
     * @param roomId : path로 받는 roomId (PK)
     * @param req : userId와 정해진 이름이 있는 객체
     * @return 다음 턴 사용자의 userId, 답이 맞았는지, 게임이 끝났는지 여부를 멤버변수로 가진 객체
     */
    @MessageMapping("/call/guess-name/{roomId}")
    @SendTo("/from/call/guess-name/{roomId}")
    public GuessNameRes guessName(@DestinationVariable long roomId, NameReq req){
        GuessNameRes guessNameRes = ManagerHolder.gameManagerMap.get(roomId).guess(req);
        // 게임 끝나면 Map에서 Manager 삭제
        if(guessNameRes.isGameEnd()) ManagerHolder.gameManagerMap.remove(roomId);
        return guessNameRes;
    }


}