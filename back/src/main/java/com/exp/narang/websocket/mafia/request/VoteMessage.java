package com.exp.narang.websocket.mafia.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteMessage {
    private String username;
    private String theVoted;
    private String stage;

    public VoteMessage() { }

    public VoteMessage(String username, String theVoted) {
        this.username = username;
        this.theVoted = theVoted;
    }

    @Override
    public String toString() {
        return "VoteMessage{" +
            "username='" + username + '\'' +
            ", theVoted='" + theVoted + '\'' +
            ", stage='" + stage + '\'' +
            '}';
    }
}
