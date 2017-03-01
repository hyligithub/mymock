package com.jd.mock.client;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Cookie;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.mockserver.model.Parameter;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpCallback.callback;
import static org.mockserver.model.HttpForward.Scheme.HTTP;
import static org.mockserver.model.HttpForward.forward;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Created by lihuiyan on 2017/3/1.
 */
public class MyMockExample {

    public static void main(String[] args) {
        mockTest();
        mockForward();
        mockCallback();
    }

    private static void mockTest() {
        new MockServerClient("127.0.0.1", 1080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/login")
                        ,
                        exactly(1)
                )
                .respond(
                        response()
                                .withStatusCode(401)
                                .withHeaders(
                                        new Header("Content-Type", "application/json; charset=utf-8"),
                                        new Header("Cache-Control", "public, max-age=86400")
                                )
                                .withBody("{ message: 'incorrect username and password combination' }")
                                .withDelay(new Delay(SECONDS, 1))
                );
    }


    private static void mockForward() {
        new MockServerClient("127.0.0.1", 1080)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/index.html"),
                        exactly(1)
                )
                .forward(
                        forward()
                                .withHost("www.mock-server.com")
                                .withPort(80)
                                .withScheme(HTTP)
                );
    }

    private static void mockCallback() {
        MockServerClient mockServer = startClientAndServer(1088);

        mockServer
                .when(
                        request()
                                .withPath("/callback")
                )
                .callback(
                        callback()
                                .withCallbackClass("com.jd.mock.callback.PrecannedTestExpectationCallback")
                );
    }

}
