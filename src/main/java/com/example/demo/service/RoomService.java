package com.example.demo.service;

import com.example.demo.dto.RoomPassRequestDto;
import com.example.demo.dto.RoomRequestDto;
import com.example.demo.dto.RoomResponseDto;
import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.RedisRepository;
import com.example.demo.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RedisRepository redisRepository;

    public Room createRoom(RoomRequestDto requestDto, User user) {


        if (roomRepository.findByName(requestDto.getName()) != null) {
            throw new IllegalArgumentException("이미 존재하는 방 이름입니다.");
        }
        if (requestDto.getName() == null) {
            throw new IllegalArgumentException("룸 이름을 입력해주세요.");
        }
        Room room = Room.create(requestDto, user);


        return roomRepository.save(room);
    }


    public RoomResponseDto enterRoom(String roomId, RoomPassRequestDto requestDto, User user) {

        Room room = roomRepository.findByroomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

        if (room.getPassword() != null) {
            if (!room.getPassword().equals(requestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }

        String name = room.getName();
        String roomImg = room.getRoomImg();
        String content = room.getContent();
        Long userCount = redisRepository.getUserCount(roomId);
        Boolean workOut = room.getWorkOut();
        return new RoomResponseDto(name, roomId, roomImg, content, userCount, workOut);

    }


    public RoomResponseDto getRoom(RoomRequestDto requestDto) {

        Room room = roomRepository.findByroomId(requestDto.getRoomId()).orElseThrow(
                () -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

        if (!room.getPassword().equals(requestDto.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String name = room.getName();
        String roomId = room.getRoomId();
        String roomImg = room.getRoomImg();
        String content = room.getContent();
        Long userCount = redisRepository.getUserCount(requestDto.getRoomId());
        Boolean workOut = room.getWorkOut();

        return new RoomResponseDto(name, roomId, roomImg, content, userCount, workOut);
    }

    //전체 방 조화
    public List<RoomResponseDto> getRooms() {
        List<Room> rooms = roomRepository.findAll();
        List<RoomResponseDto> allRooms = new ArrayList<>();

        for (Room room : rooms) {
            allRooms.add(new RoomResponseDto(
                    room.getName(),
                    room.getRoomId(),
                    room.getRoomImg(),
                    room.getContent(),
                    room.getWorkOut()
            ));
        }
        return allRooms;
    }

    public void workout(RoomRequestDto requestDto) {
        Room room = roomRepository.findByroomId(requestDto.getRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
        room.setWorkOut(requestDto.getWorkOut());
    }
}
