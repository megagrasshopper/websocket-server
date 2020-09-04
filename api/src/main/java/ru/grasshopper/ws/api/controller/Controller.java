package ru.grasshopper.ws.api.controller;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.grasshopper.ws.api.service.UserService;
import ru.grasshopper.ws.common.domain.UserDataDto;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class Controller {

    private final UserService userService;

    @GetMapping(value = "/sleepinguser", produces = APPLICATION_JSON_VALUE)
    public List<UserDataDto> sleepingUsers() {
        return userService.getSleepingUsers();
    }
}