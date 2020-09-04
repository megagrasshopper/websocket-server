package ru.grasshopper.ws.socket.service;

import ru.grasshopper.ws.common.domain.UserData;

public interface RequestProcessor {
    void processUrlRequest(UserData userData);

    void processSessionClose(String sessionId);

    void processPong(String sessionId);

    boolean isPongExists(String sessionId);
}
