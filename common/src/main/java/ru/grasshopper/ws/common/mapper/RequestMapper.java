package ru.grasshopper.ws.common.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.grasshopper.ws.common.domain.ClientRequest;
import ru.grasshopper.ws.common.domain.UserData;

@Component
@RequiredArgsConstructor
@Slf4j
public class RequestMapper {
    private final ObjectMapper objectMapper;

    public Optional<UserData> getUserData(String sessionId, String msg) {
        try {
            return Optional.of(UserData.of(sessionId, objectMapper.readValue(msg, ClientRequest.class)));
        } catch (IOException ex) {
            log.warn("Invalid JSON: " + msg, ex);
            return Optional.empty();
        }
    }
}