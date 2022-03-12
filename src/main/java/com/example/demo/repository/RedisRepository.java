package com.example.demo.repository;

import com.example.demo.model.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RedisRepository {
    // Redis CacheKeys
    public static final String USER_COUNT = "USER_COUNT";
    public static final String ENTER_INFO = "ENTER_INFO";
    public static final String USER_INOUT = "USER_INOUT";

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOps;
    @Resource(name = "redisTemplate")
    private HashOperations<String, String, String> stringHashOpsEnterInfo;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Integer> longOperations;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, Boolean> userInOutOperations;




    // sessionId로 inOutKey 등록
    public void setSessionUserInfo(String sessionId, Long roomId, String name) {
        System.out.println("setSessionUserInfo");
        stringHashOpsEnterInfo.put(ENTER_INFO, sessionId, roomId + "_" + name);
    }

    // sessionId로 inOutKey 찾아오기
    public String getSessionUserInfo(String sessionId) {
        System.out.println("getSessionUserInfo");
        return stringHashOpsEnterInfo.get(ENTER_INFO, sessionId);
    }

    // sessionId 삭제
    public void removeUserEnterInfo(String sessionId) {
        System.out.println("removeUserEnterInfo");
        stringHashOpsEnterInfo.delete(ENTER_INFO, sessionId);
    }

    // inOutKey로 현재 유저가 접속 중인지 설정
    public void setUserChatRoomInOut(String key, Boolean inOut) {
        System.out.println("setUserChatRoomInOut");
        userInOutOperations.set(USER_INOUT + "_" + key, inOut);
    }

    // inOutKey로 현재 유저가 접속 중인지 가져오기
    public Boolean getUserChatRoomInOut(Long roomId, String name) {
        return Optional.ofNullable(userInOutOperations.get(USER_INOUT + "_" + roomId + "_" + name)).orElse(false);
    }

    // 채팅방 유저수 조회
    public long getUserCount(Long roomId) {
        return Long.valueOf(Optional.ofNullable(valueOps.get(USER_COUNT + "_" + roomId)).orElse("0"));
    }
        // 채팅방에 입장한 유저수 +1
        public long plusUserCount(Long roomId) {
            return Optional.ofNullable(valueOps.increment(USER_COUNT + "_" + roomId)).orElse(0L);
        }

        // 채팅방에 입장한 유저수 -1
        public long minusUserCount(Long roomId) {
            return Optional.ofNullable(valueOps.decrement(USER_COUNT + "_" + roomId)).filter(count -> count > 0).orElse(0L);
        }
    }
