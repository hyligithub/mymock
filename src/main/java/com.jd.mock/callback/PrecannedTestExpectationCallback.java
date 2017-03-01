package com.jd.mock.callback;

import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpResponse.notFoundResponse;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.HttpStatusCode.ACCEPTED_202;

/**
 * Created by lihuiyan on 2017/3/1.
 */
public class PrecannedTestExpectationCallback implements ExpectationCallback {

    public static HttpResponse httpResponse = response()
            .withStatusCode(ACCEPTED_202.code())
            .withHeaders(
                    header("x-callback", "test_callback_header"),
                    header("Content-Length", "a_callback_response".getBytes().length),
                    header("Connection", "keep-alive")
            )
            .withBody("a_callback_responseaaa");

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        if (httpRequest.getPath().getValue().endsWith("/callback")) {
            return httpResponse;
        } else {
            return notFoundResponse();
        }
    }
}