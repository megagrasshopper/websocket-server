package ru.grasshopper.ws.socket.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import ru.grasshopper.ws.common.mapper.RequestMapper;
import ru.grasshopper.ws.socket.config.NodeConfig;
import ru.grasshopper.ws.socket.service.RequestProcessor;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiveHandler {
    private final RequestMapper requestMapper;
    private final RequestProcessor requestProcessor;
    private final NodeConfig nodeConfig;

    public Mono<Void> handle(WebSocketSession session) {

        String sessionId = nodeConfig.getPrefix() + session.getId();
        log.debug("sessionId = " + sessionId);
        return session.receive()
                .doOnSubscribe(msg -> requestProcessor.processPong(sessionId))
                .doOnNext(msg -> {

                    switch (msg.getType()) {
                        case PONG:
                            requestProcessor.processPong(sessionId);
                            break;
                        case TEXT:
                            requestMapper.getUserData(sessionId, msg.getPayloadAsText())
                                    .ifPresent(requestProcessor::processUrlRequest);
                    }
                })
                .doFinally(signalType -> {
                    log.info("Terminating WebSocket Session (client side) sig: [{}], [{}]", signalType.name(), sessionId);
                    requestProcessor.processSessionClose(sessionId);
                    session.close();
                })
                .log()
                .then();
    }
}
