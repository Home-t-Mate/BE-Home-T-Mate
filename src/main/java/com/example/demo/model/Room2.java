package com.example.demo.model;


import org.springframework.web.socket.WebSocketSession;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NotNull
public class Room2 {

    /**
     * 채팅방은 id, Clients로 구성
     */

    private final Long id;
    // sockets by user names

    // WebSocketSession은 spring에서 WebSocket connection이 맺어진 세션을 가리킨다 - 편하게 고수준 socket이라고 생각
    //
    private final Map<String, WebSocketSession> clients = new HashMap<>();

    public Room2(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    Map<String, WebSocketSession> getClients() {
        return clients;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Room2 room = (Room2) o;
        return Objects.equals(getId(), room.getId()) &&
                Objects.equals(getClients(), room.getClients());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getClients());
    }
}