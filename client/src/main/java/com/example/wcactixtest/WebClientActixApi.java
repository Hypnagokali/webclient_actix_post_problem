package com.example.wcactixtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class WebClientActixApi {

    private final WebClient webClient;

    public WebClientActixApi() {
        webClient = WebClient.builder()
            .baseUrl("http://localhost:7070")
            .build();
    }

    public Mono<Void> doTestRequest() {
        log.info( "start do-some-action" );
        return webClient.post().uri( "/do-some-action/123" )
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> doTestRequestThatWorks() {
        log.info( "start do-some-action" );
        return webClient.post().uri( "/do-some-action/123" )
            .bodyValue( "" )
            .retrieve()
            .bodyToMono(Void.class);
    }
}
