package com.example.wcactixtest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
class WebClientActixApiTest {

    static int postCount = 1;

    @Autowired
    WebClientActixApi webClientActixApi;

    @Test
    void hangsAfterFirstOrSecondRequest() {
        StepVerifier.create( webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatHangsAfterSomeCalls() ).verifyComplete();
    }

    @Test
    void worksWhenUsingByteExtractorOnActixSide() {
        StepVerifier.create( webClientActixApi.doSendTestRequestThatIsReadByByteExtractor() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatIsReadByByteExtractor() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatIsReadByByteExtractor() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatIsReadByByteExtractor() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendTestRequestThatIsReadByByteExtractor() ).verifyComplete();
    }

    @Test
    void worksWhenSendingContentLength() {
        StepVerifier.create( webClientActixApi.doSendWithContentLength0() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendWithContentLength0() ).verifyComplete();
        StepVerifier.create( webClientActixApi.doSendWithContentLength0() ).verifyComplete();
    }

    @Test
    void sendFilesTest() {
        StepVerifier.create( webClientActixApi.sendFiles() ).verifyComplete();
        StepVerifier.create( webClientActixApi.sendFiles() ).verifyComplete();
        StepVerifier.create( webClientActixApi.sendFiles() ).verifyComplete();
    }

    @Test
    void send_multipart_raw() {
        String host = "localhost";
        int port = 7070;
        String hostPort = "localhost:" + port;

        try (Socket socket = new Socket( host, port )) {
            BufferedReader reader = new BufferedReader( new InputStreamReader(
                socket.getInputStream(),
                StandardCharsets.UTF_8
            ) );

            OutputStream out = socket.getOutputStream();

            // First request
            doPost( out, hostPort );
            readEmptyResponse( reader );

            // Second request
            doPost( out, hostPort );
            readEmptyResponse( reader );

            // Third request
            doPost( out, hostPort );
            readEmptyResponse( reader );

        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    private static void readEmptyResponse(BufferedReader reader) throws IOException {
        System.out.printf("Try reading response (%d)%n", postCount);
        postCount++;
        String line;
        while ( ( line = reader.readLine() ) != null ) {
            System.out.println( "Next data from Server: " + line + "(length: " + line.length() + ")" );
            if ( line.isBlank() ) {
                break;
            }
        }
    }

    private static void doPost(OutputStream out, String hostPort)
        throws IOException {
        System.out.printf( "Do Multipart POST chunked (%d)%n", postCount );
        String fileContent = "Hello, this is a text file content!";
        int length = fileContent.getBytes( StandardCharsets.UTF_8 ).length;
        String fileName = "test.txt";
        String boundary = "SomeRandomBoundary";

        BufferedWriter writer = new BufferedWriter( new OutputStreamWriter( out, StandardCharsets.UTF_8 ) );

        // Start of HTTP request
        writer.write( "POST /upload/888 HTTP/1.1\r\n" );
        writer.write( "host: " + hostPort + "\r\n" );
        writer.write( "user-agent: DemoAgent\r\n" );
        writer.write( "accept: */*\r\n" );
        writer.write( "transfer-encoding: chunked\r\n" );
        writer.write( "content-type: multipart/form-data;boundary=" + boundary + "\r\n" );
        writer.write( "connection: keep-alive\r\n" );
        writer.write( "\r\n" );
        writer.flush();

        // Some hardcoded chunks
        byte[] chunk1 = ( "--" + boundary + "\r\n" ).getBytes( StandardCharsets.UTF_8 );
        int len1 = chunk1.length;
        byte[] chunk2 = ( "Content-Disposition: form-data; name=\"file\"; filename=\"" + fileName + "\"\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: " + length + "\r\n\r\n" ).getBytes( StandardCharsets.UTF_8 );
        int len2 = chunk2.length;
        byte[] chunk3 = fileContent.getBytes( StandardCharsets.UTF_8 );
        int len3 = chunk3.length;
        byte[] chunk3b = ( "\r\n" ).getBytes( StandardCharsets.UTF_8 );
        int len3b = chunk3b.length;
        byte[] chunk4 = ( "--" + boundary + "--\r\n" ).getBytes( StandardCharsets.UTF_8 );
        int len4 = chunk4.length;

        // Chunk 1
        out.write( ( Integer.toHexString( len1 ) + "\r\n" ).getBytes( StandardCharsets.UTF_8 ) );
        out.write( chunk1 );
        out.write( "\r\n".getBytes( StandardCharsets.UTF_8 ) );
        // Chunk 2
        out.write( ( Integer.toHexString( len2 ) + "\r\n" ).getBytes( StandardCharsets.UTF_8 ) );
        out.write( chunk2 );
        out.write( "\r\n".getBytes( StandardCharsets.UTF_8 ) );
        // Chunk 3
        out.write( ( Integer.toHexString( len3 ) + "\r\n" ).getBytes( StandardCharsets.UTF_8 ) );
        out.write( chunk3 );
        out.write( "\r\n".getBytes( StandardCharsets.UTF_8 ) );
        out.write( ( Integer.toHexString( len3b ) + "\r\n" ).getBytes( StandardCharsets.UTF_8 ) );
        out.write( chunk3b );
        out.write( "\r\n".getBytes( StandardCharsets.UTF_8 ) );
        // Chunk 4
        out.write( ( Integer.toHexString( len4 ) + "\r\n" ).getBytes( StandardCharsets.UTF_8 ) );
        out.write( chunk4 );
        out.write( "\r\n".getBytes( StandardCharsets.UTF_8 ) );

        // End chunk
        out.write( "0\r\n\r\n".getBytes( StandardCharsets.UTF_8 ) );

        System.out.println( "Force flush" );
        out.flush();
    }
}