package ru.grasshopper.ws.client;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage.Type;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

public class MultiClient {

    public static void main(String[] args) throws Exception {
        int clients = args.length > 0 ? Integer.parseInt(args[0]) : 25_000;

        WebSocketHandler handler =
                session -> session.receive()
                        .filter(msg -> msg.getType() == Type.PING)
                        .doOnNext(msg -> session.send(
                                Mono.just(session.pongMessage(dbf -> session.bufferFactory()
                                        .wrap("pong message".getBytes(StandardCharsets.UTF_8))))
                                ).subscribe()
                        )
                        .log()
                        .then();

        for (int i = 0; i < clients; i++) {
            var client = new ReactorNettyWebSocketClient();
            var exec = client.execute(URI.create("ws://localhost:9090/ws"), handler);
            exec.subscribe();
        }

        Thread.currentThread().join();
    }


}
