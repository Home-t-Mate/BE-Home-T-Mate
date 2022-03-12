package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public Room createRoom(String name, Optional<User> user) {
        Room room = new Room(name);
        return roomRepository.save(room);
    }

    public Optional<Room> findRoom(Long roomId) {
        return roomRepository.findById(roomId);
    }
}
