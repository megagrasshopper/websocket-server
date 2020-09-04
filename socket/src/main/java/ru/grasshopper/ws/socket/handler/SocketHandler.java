package ru.grasshopper.ws.socket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class SocketHandler implements WebSocketHandler {

    private final ReceiveHandler receiveHandler;
    private final PingHandler pingHandler;

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        
        return Mono.zip(receiveHandler.handle(session), pingHandler.handle(session)).then();
    }
}
