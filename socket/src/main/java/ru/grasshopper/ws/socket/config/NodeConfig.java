package ru.grasshopper.ws.socket.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.socket.WebSocketSession;

@Configuration
@Getter
@Setter
public class NodeConfig {
    @Value("${node-prefix:}")
    private String prefix;

    public String getSessionId(WebSocketSession session) {
        return prefix + session.getId();
    }
}