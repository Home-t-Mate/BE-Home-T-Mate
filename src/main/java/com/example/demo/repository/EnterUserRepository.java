package com.example.demo.repository;

import com.example.demo.model.EnterUser;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnterUserRepository extends JpaRepository<EnterUser, Long > {
    List<EnterUser> findByRoom(Room room);

//    EnterUser findByRoomAndUser(Optional<Room> room, User user);

    EnterUser deleteByRoomAndUser(Optional<Room> room, User user);

    EnterUser findByRoomAndUser(Room room, User user);
}
