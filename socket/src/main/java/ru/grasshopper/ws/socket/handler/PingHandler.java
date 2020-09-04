package ru.grasshopper.ws.socket.handler;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.grasshopper.ws.socket.config.NodeConfig;
import ru.grasshopper.ws.socket.service.RequestProcessor;

@Component
@RequiredArgsConstructor
@Slf4j
public class PingHandler {

    private final RequestProcessor processor;
    private final NodeConfig nodeConfig;
    @Value("${ws.keepalive-timeout-in-seconds}")
    private Long keepAliveMessageTimeout;

    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = nodeConfig.getSessionId(session);
        return session.send(
                Flux.interval(Duration.ofSeconds(keepAliveMessageTimeout))
                        .map(it -> session.pingMessage(dbf -> session.bufferFactory()
                                        .wrap("ping message".getBytes(StandardCharsets.UTF_8))
                                )
                        )
                        .doOnNext(msg -> {
                            //if there is no saved pong messages, close session
                            if (!processor.isPongExists(sessionId)) {
                                processor.processSessionClose(sessionId);
                                session.close();
                            }
                        })
        ).log().then();
    }
}