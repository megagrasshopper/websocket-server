package ru.grasshopper.ws.common.domain;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class UserData implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String sessionId;
    private final String ip;
    private final String userAgent;
    private final String url;
    private final Long timestamp;

    public static UserData of(String sessionId, ClientRequest request) {
        return new UserData(sessionId, request.getIp(), request.getUserAgent(), request.getUrl(),
                System.currentTimeMillis());
    }
}