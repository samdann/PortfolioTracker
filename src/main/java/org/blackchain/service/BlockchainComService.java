package org.blackchain.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.blackchain.model.blockchain.com.TxOutput;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BlockchainComService {

     private static final String URL = "https://blockchain.info/rawaddr/";

     public BlockchainAddress getBitcoinAddress(final String address) {
          String responseString = executeGetRequest(address);
          Gson gson = new Gson();
          BlockchainAddress blockchainAddress = gson.fromJson(responseString,
                  BlockchainAddress.class);
          blockchainAddress.getTxs().forEach(tx -> {
               List<TxOutput> filteredList = tx.getOutputs().stream()
                       .filter(out -> address.equalsIgnoreCase(out.getAddress()))
                       .collect(Collectors.toList());
               tx.setOutputs(filteredList);
          });

          return blockchainAddress;
     }

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
