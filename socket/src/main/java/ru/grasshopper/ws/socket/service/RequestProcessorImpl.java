package ru.grasshopper.ws.socket.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.springframework.stereotype.Service;
import ru.grasshopper.ws.common.config.MapNames;
import ru.grasshopper.ws.common.domain.UserData;

@Service
public class RequestProcessorImpl implements RequestProcessor {

    private final IMap<String, UserData> onlineUsers;
    private final IMap<String, UserData> sleepingUsers;
    private final IMap<String, String> pong;

    public RequestProcessorImpl(HazelcastInstance hz) {
        onlineUsers = hz.getMap(MapNames.ONLINE);
        sleepingUsers = hz.getMap(MapNames.SLEEPING);
        pong = hz.getMap(MapNames.PONG);
    }

    @Override
    public void processUrlRequest(UserData userData) {
        onlineUsers.putAsync(userData.getSessionId(), userData);
        sleepingUsers.putAsync(userData.getSessionId(), userData);
        processPong(userData.getSessionId());
    }

    @Override
    public void processSessionClose(String sessionId) {
        sleepingUsers.delete(sessionId);
        onlineUsers.delete(sessionId);
        pong.delete(sessionId);
    }

    @Override
    public void processPong(String sessionId) {
        pong.putAsync(sessionId, sessionId);
    }

    @Override
    public boolean isPongExists(String sessionId) {
        return pong.get(sessionId) != null;
    }
}