package com.example.BidZone.websocketinterface;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Data
@AllArgsConstructor
class Greeting {
    private String content;
}

@Data
class HelloWorldRequest {
    private String name;
}


@Controller
public class TestWebsocketController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting() {
        return new Greeting("Hello from server");
    }
}
