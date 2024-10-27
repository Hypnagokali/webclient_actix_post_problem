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

    public Mono<Void> doSendTestRequestThatHangsAfterSomeCalls() {
        log.info( "start do-some-action (without Content-Length) -> hangs after some calls" );
        return webClient.post().uri( "/do-some-action" )
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> doSendTestRequestThatIsReadByByteExtractor() {
        log.info( "do-some-action-read-body (without Content-Length) -> works" );
        return webClient.post().uri( "/do-some-action-read-body" )
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> doSendWithContentLength0() {
        log.info( "start do-some-action (with Content-Length) -> works" );
        return webClient.post().uri( "/do-some-action" )
            .contentLength( 0 )
            .retrieve()
            .bodyToMono(Void.class);
    }

}
