package com.exp.narang.websocket.mafia.model.manager;

import com.exp.narang.api.service.RoomService;
import com.exp.narang.api.service.RoomServiceImpl;
import com.exp.narang.db.entity.Room;
import com.exp.narang.db.entity.User;
import com.exp.narang.websocket.mafia.model.service.GamePlayers;

import com.exp.narang.websocket.mafia.model.service.GameResult;
import com.exp.narang.websocket.mafia.request.VoteMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Getter
@Setter
@Component
// 게임에 필요한 로직을 관리한다.
public class GameManager {
    private static final Logger log = LoggerFactory.getLogger(GameManager.class);

    private RoomService roomService;
    private Long roomId;
    private GamePlayers gamePlayers;
    private VoteManager voteManager;
    private String roleString;

    public GameManager () {}

    public GameManager (Long roomId, RoomService roomService) {
        this.roomService = roomService;
        this.roomId = roomId;
        List<User> users = roomService.findUserListByRoomId(roomId);
        this.gamePlayers = new GamePlayers(users);
        RoleManager.assignRoleToPlayers(this.gamePlayers);
//        this.roleString = gamePlayers.getRoleString();
//        this.voteManager = new VoteManager(gamePlayers);
    }

//    public GameManager(Set<User> users) {
//        this.players = new GamePlayers(users);
//        RoleManager.assignRoleToPlayers(this.players);
//        this.roleString = players.getRoleString();
//        this.voteManager = new VoteManager(players);
//    }
//
    public String findRoleNameByUsername(String username) {
        return this.gamePlayers.findRoleName(username);
    }

//    public GameResult returnVoteResult(VoteMessage voteMessage) {
//        log.debug("returnVoteResult: {}", voteMessage);
//        if (!voteManager.handleVote(voteMessage)) {
//            return GameResult.votingStatus();
//        }
//        GameResult gr = voteManager.returnGameResult(voteMessage.getStage());
//        if (gr.isFinished()) {
//            gr.setRoleString(roleString);
//        }
//        return gr;
//    }
}
