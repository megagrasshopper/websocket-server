package ru.grasshopper.ws.api.service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.query.Predicates;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import ru.grasshopper.ws.api.config.SleepingConfig;
import ru.grasshopper.ws.common.config.MapNames;
import ru.grasshopper.ws.common.domain.UserData;
import ru.grasshopper.ws.common.domain.UserDataDto;

@Service
public class UserServiceImpl implements UserService {
    private final IMap<String, UserData> sleepingUsers;
    private final SleepingConfig sleepingConfig;

    public UserServiceImpl(HazelcastInstance hz, SleepingConfig sleepingConfig) {
        sleepingUsers = hz.getMap(MapNames.SLEEPING);
        this.sleepingConfig = sleepingConfig;
    }

    @Override
    public List<UserDataDto> getSleepingUsers() {

        long now = System.currentTimeMillis();

        return sleepingUsers.values(
                Predicates.lessEqual("timestamp",
                        now - sleepingConfig.getSleepingTimeMilli()))
                .stream()
                .map(UserDataDto::of)
                .peek(user -> user.setTimeSec((now - user.getTimestamp()) / 1000.))
                .collect(Collectors.toList());
    }
}