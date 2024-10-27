package com.example.wcactixtest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class WebClientActixApiTest {

    @Autowired
    WebClientActixApi webClientActixApi;

    @Test
    void hangsAfterFirstOrSecondRequest() {
        StepVerifier.create(webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls()).verifyComplete();
    }

    @Test
    void worksWhenUsingByteExtractorOnActixSide() {
        StepVerifier.create(webClientActixApi.doSendTestRequestThatIsReadByByteExtractor()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatIsReadByByteExtractor()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatIsReadByByteExtractor()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatIsReadByByteExtractor()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendTestRequestThatIsReadByByteExtractor()).verifyComplete();
    }

    @Test
    void worksWhenSendingContentLength() {
        StepVerifier.create(webClientActixApi.doSendWithContentLength0()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendWithContentLength0()).verifyComplete();
        StepVerifier.create(webClientActixApi.doSendWithContentLength0()).verifyComplete();
    }

}