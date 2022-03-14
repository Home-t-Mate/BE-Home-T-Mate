package com.example.demo.service;

import com.example.demo.dto.RoomRequestDto;
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

    public Room createRoom(RoomRequestDto requestDto, User user) {


        if (roomRepository.findByName(requestDto.getName())) {
            throw new IllegalArgumentException("이미 존재하는 방 이름입니다.");
        }

        if (requestDto.getName() == null) {
            throw new IllegalArgumentException("룸 이름을 입력해주세요.");
        }


        Room room = Room.create(requestDto, user);
        return roomRepository.save(room);
    }


    public Optional<Room> findRoom(String roomId) {

        if (!roomRepository.findByroomId(roomId).isPresent()) {
            throw new IllegalArgumentException("존재하지 않는 방입니다.");
        }
        return roomRepository.findByroomId(roomId);
    }
}
