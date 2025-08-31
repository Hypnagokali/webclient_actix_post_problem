package com.example.wcactixtest;

import java.io.File;
import java.net.URI;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
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

    @SneakyThrows
    public Mono<Void> sendFiles() {
        log.info( "send multipart" );
        URI uri = getClass().getResource( "/hasen.txt" ).toURI();
        File file = new File( uri );
        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part( "file", new FileSystemResource( file ), MediaType.TEXT_PLAIN);
        return webClient.post().uri( "/upload/133" )
            .body( BodyInserters.fromMultipartData( multipartBodyBuilder.build() ) )
//            .header( "Connection", "close" )
            .retrieve()
            .bodyToMono(Void.class);
    }


    public Mono<Void> doSendTestRequestThatHangsAfterSomeCalls() {
        log.info( "start do-some-action (without Content-Length) -> hangs after some calls" );
        return webClient.post().uri( "/do-some-action/22" )
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
        return webClient.post().uri( "/do-some-action/33" )
            .contentLength( 0 )
            .retrieve()
            .bodyToMono(Void.class);
    }

}
