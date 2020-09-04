package ru.grasshopper.ws.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import org.springframework.web.reactive.socket.WebSocketMessage.Type;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.grasshopper.ws.common.domain.ClientRequest;

public class SocketClient {

    private static final ObjectMapper om = new ObjectMapper();

    public static void main(String... args) {

        List<ClientRequest> messages = List.of(new ClientRequest()
                        .setIp("ip1")
                        .setUrl("url1")
                        .setUserAgent("uagent 1"),
                new ClientRequest()
                        .setIp("ip1")
                        .setUrl("url2")
                        .setUserAgent("uagent 1"),
                new ClientRequest()
                        .setIp("ip1")
                        .setUrl("url3")
                        .setUserAgent("uagent 1"));

        var client = new ReactorNettyWebSocketClient();

        Mono<Void> exec = client.execute(URI.create("ws://localhost:9090/ws"),
                session -> session.send(
                        Flux.fromIterable(messages)
                                .map(SocketClient::requestToString)
                                .map(session::textMessage)
                )
                        .thenMany(session.receive()
                                .filter(msg -> msg.getType() == Type.PING)
                                .doOnNext(msg -> session.send(
                                        Mono.just(session.pongMessage(dbf -> session.bufferFactory()
                                                .wrap("pong message".getBytes(StandardCharsets.UTF_8))))
                                        ).subscribe()
                                )
                        )
                        .log()
                        .then()
        );


        exec.block(Duration.ofSeconds(200));
    }

    private static String requestToString(ClientRequest request) {
        try {
            return om.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
