package ru.grasshopper.ws.api.service;

import java.util.List;
import ru.grasshopper.ws.common.domain.UserDataDto;

public interface UserService {
    List<UserDataDto> getSleepingUsers();
}