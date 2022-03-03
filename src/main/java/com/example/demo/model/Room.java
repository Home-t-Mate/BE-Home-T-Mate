package com.example.demo.model;


import com.example.demo.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Setter
@Getter
@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    @Column
    private  String name;

    @Column
    private Long userCount;

    @ManyToOne
    @JoinColumn
    private User user;

    public Room(String name, long userCount) {
        this.name = name;
        this.userCount = userCount;
//        this.user = user;
    }

    public Room(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public Room(String name) {
        this.name = name;
    }

    public Room() {

    }

    public static Room create(String name) {
        Room room = new Room();
        room.roomId = Long.valueOf(UUID.randomUUID().toString());
        room.name = name;
        return room;
    }
}
