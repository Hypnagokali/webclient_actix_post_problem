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
    void hangsAfterSecondRequest() {
        StepVerifier.create(webClientActixApi.doTestRequest()).verifyComplete();
        StepVerifier.create(webClientActixApi.doTestRequest()).verifyComplete();
        StepVerifier.create(webClientActixApi.doTestRequest()).verifyComplete();
    }

    @Test
    void withEmptyStringAsBodyItWorks() {
        StepVerifier.create(webClientActixApi.doTestRequestThatWorks()).verifyComplete();
        StepVerifier.create(webClientActixApi.doTestRequestThatWorks()).verifyComplete();
        StepVerifier.create(webClientActixApi.doTestRequestThatWorks()).verifyComplete();

    }
}