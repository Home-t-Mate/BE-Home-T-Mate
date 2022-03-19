package com.example.demo.service;

import com.example.demo.dto.RoomPassRequestDto;
import com.example.demo.dto.RoomRequestDto;
import com.example.demo.dto.RoomResponseDto;
import com.example.demo.model.EnterUser;
import com.example.demo.model.User;
import com.example.demo.model.Room;
import com.example.demo.repository.EnterUserRepository;
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
    private final EnterUserRepository enterUserRepository;
    private final int LIMIT = 5;

    public Room createRoom(RoomRequestDto requestDto, User user) {

        if (roomRepository.findByName(requestDto.getName()) != null) {
            throw new IllegalArgumentException("이미 존재하는 방 이름입니다.");
        }
        if (requestDto.getName() == null) {
            throw new IllegalArgumentException("룸 이름을 입력해주세요.");
        }
        Room room = Room.create(requestDto, user);
        boolean passworkCheck = requestDto.getPassword() != null ? true : false;
        room.setPassCheck(passworkCheck);

        return roomRepository.save(room);
    }



    public RoomResponseDto enterRoom(String roomId, RoomPassRequestDto requestDto, User user) {

        Room room = roomRepository.findByroomId(roomId).orElseThrow(
                () -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

        List<EnterUser> enterUserSize = enterUserRepository.findByRoom(room);


        if (enterUserSize.size() > 0 ) {
            if (LIMIT < enterUserSize.size() + 1) {
                throw new IllegalArgumentException("입장인원을 초과하였습니다.");
            }
        }
        if (room.getPassCheck()) {
            if (!room.getPassword().equals(requestDto.getPassword())) {
                throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
            }
        }


        EnterUser enterUser = new EnterUser(user, room);
        enterUserRepository.save(enterUser);


        String name = room.getName();
        String roomImg = room.getRoomImg();
        String content = room.getContent();
        Long userCount = redisRepository.getUserCount(roomId);
        Boolean passCheck = room.getPassCheck();
        Boolean workOut = room.getWorkOut();
        int count = room.getCount();
        return new RoomResponseDto(name, roomId, roomImg, content, userCount, passCheck, workOut, count);

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
        Boolean passCheck = room.getPassCheck();
        Boolean workOut = room.getWorkOut();
        int count = room.getCount();

        return new RoomResponseDto(name, roomId, roomImg, content, userCount, passCheck, workOut, count);
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
                    room.getPassCheck(),
                    room.getWorkOut(),
                    room.getCount()
            ));
        }
        return allRooms;
    }

    public void workout(RoomRequestDto requestDto) {
        Room room = roomRepository.findByroomId(requestDto.getRoomId()).orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
        room.setWorkOut(requestDto.getWorkOut());
    }

    public void quitRoom(String roomId, User user) {
        Room room = roomRepository.findByroomId(roomId).orElseThrow(()-> new IllegalArgumentException("해당 방이 존재하지 않습니다."));
        EnterUser enterUser =  enterUserRepository.findByRoomAndUser(room, user);
        enterUserRepository.delete(enterUser);
    }

    public void deleteRoom(String roomId, User user) {

        Room room = roomRepository.findByroomId(roomId).orElseThrow(() -> new IllegalArgumentException("해당 방이 존재하지 않습니다."));

        if(!room.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("방을 만든 유저만 삭제할 수 있습니다.");
        }
        roomRepository.delete(room);
    }
}
