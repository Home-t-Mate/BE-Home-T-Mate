package com.example.demo.service;

import com.example.demo.dto.RoomResponseDto;
import com.example.demo.model.Room;
import com.example.demo.model.User;
import com.example.demo.repository.RoomRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public List<RoomResponseDto> search(String keyword) {

        List<Room> roomNames = roomRepository.findByNameContainingIgnoreCase(keyword);
        List<User> users = userRepository.findByNicknameContainingIgnoreCase(keyword);
        List<RoomResponseDto> allRooms = new ArrayList<>();

        //방 만든 사람 기준으로 검색
        if (users != null) {
            for(User user : users) {
                List<Room> roomUsers = roomRepository.findByUser(user);
                for (Room room : roomUsers) {
                    allRooms.add(new RoomResponseDto(
                            room.getName(),
                            room.getRoomId(),
                            room.getRoomImg(),
                            room.getContent(),
                            room.getUserCount(),
                            room.getPassCheck(),
                            room.getWorkOut(),
                            room.getUser().getNickname(),
                            room.getUser().getProfileImg()
                    ));
                }

            }
        }

        //방 이름 기준으로 검색
        if (roomNames != null) {
            for (Room room : roomNames) {
                allRooms.add(new RoomResponseDto(
                        room.getName(),
                        room.getRoomId(),
                        room.getRoomImg(),
                        room.getContent(),
                        room.getUserCount(),
                        room.getPassCheck(),
                        room.getWorkOut(),
                        room.getUser().getNickname(),
                        room.getUser().getProfileImg()
                ));
            }
        }
        return allRooms;
    }
}
