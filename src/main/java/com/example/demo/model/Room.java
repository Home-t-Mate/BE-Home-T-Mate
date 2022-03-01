package com.example.demo.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    public Room(String name, Long userCount) {
        this.name = name;
        this.userCount = userCount;
    }

    public Room(String name) {
        this.name = name;
    }

    public Room() {

    }
}
