package org.blackchain.service;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlockchainService {

     private static final String URL = "https://blockchain.info/rawaddr/";

     public String executeGetRequest(final String requestPath) {
          OkHttpClient client = new OkHttpClient().newBuilder().build();

          String url = URL + requestPath;
          Request request = new Request.Builder().url(url)
                  .addHeader("Content-Type", "application/json").build();

          ResponseBody responseBody;
          try {
               responseBody = client.newCall(request).execute().body();
          } catch (IOException e) {
               throw new RuntimeException(e);
          }
          assert responseBody != null;

          String responseString;
          try {
               responseString = responseBody.string();
          } catch (IOException e) {
               log.error("Error reading response body: {}", e.getMessage());
               return null;
          }
          log.info(responseString);
          return responseString;
     }
}
