package ru.grasshopper.ws.common.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class UserDataDto {

    private final String sessionId;
    private final String ip;
    private final String userAgent;
    private final String url;
    private final Long timestamp;
    @Setter
    private double timeSec;

    public static UserDataDto of(UserData ud) {
        return new UserDataDto(ud.getSessionId(), ud.getIp(), ud.getUserAgent(), ud.getUrl(), ud.getTimestamp());
    }
}