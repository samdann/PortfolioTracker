package org.blackchain.util;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Slf4j
public class Requests {

    public static void getRequest() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        //RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://api.exchange.coinbase.com/accounts")
                .addHeader("Content-Type", "application/json")
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        log.info(response.body().string());

    }

}
