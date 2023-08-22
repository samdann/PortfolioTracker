package org.blackchain.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.codec.binary.Hex;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.coinbase.candle.CBCandles;
import org.blackchain.model.coinbase.product.CBProduct;
import org.blackchain.model.coinbase.product.CBProducts;
import org.blackchain.util.EthereumUtils;
import org.blackchain.util.UrlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@PropertySource("secret.properties")
@Service
public class CoinbaseService {

     // API constants
     private static final String CB_ACCESS_KEY = "cb-access-key";
     private static final String CB_ACCESS_SECRET = "cb-access-passphrase";
     private static final String CB_ACCESS_TIMESTAMP = "cb-access-timestamp";
     private static final String CB_ACCESS_SIGN = "cb-access-sign";
     private static final String HMAC_SHA_256 = "HmacSHA256";

     // API Url
     private static final String COINBASE_BASE_URL = "https://api.coinbase.com";
     private static final String COINBASE_PATH_PRODUCTS = "/api/v3/brokerage/products";
     private static final String COINBASE_PATH_PRODUCT_CANDLES = "/api/v3/brokerage/products/{product_id}/candles";

     @Value("${coinbase.api.key}")
     private String coinbaseApiKey;

     @Value("${coinbase.api.passphrase}")
     private String coinbaseApiPassPhrase;

     public List<CBProduct> getCoinbaseProducts(final String ticker) {
          log.info("Reading all products" + (StringUtils.hasLength(ticker)
                  ? " with a ticker: {}" : ""), ticker);
          final List<CBProduct> result = new ArrayList<>();

          String responseString = executeGetRequest(COINBASE_BASE_URL,
                  COINBASE_PATH_PRODUCTS, null, null);

          Gson gson = new Gson();
          CBProducts productList = gson.fromJson(responseString, CBProducts.class);
          if (StringUtils.hasLength(ticker)) {
               result.addAll(productList.getProducts().stream().filter(product ->
                       product.getBase_display_symbol().equalsIgnoreCase(ticker)
                               && product.getProduct_id()
                               .endsWith(EthereumUtils.SUPPORTED_CURRENCIES)).toList());
          }

          log.info("...found {} products with ticker {}", result.size(), ticker);
          return result;
     }

     public List<CBCandle> getProductHistoricData(final String productId,
             final Map<String, String> queryParams) {
          log.info("Retrieving historic price data for product: {}", productId);

          final String requestPath = COINBASE_PATH_PRODUCT_CANDLES.replace("{product_id}",
                  productId);
          String requestParams = UrlUtils.addQueryParams(queryParams);
          String responseString = executeGetRequest(COINBASE_BASE_URL, requestPath,
                  requestParams, null);

          Gson gson = new Gson();
          CBCandles candleList = gson.fromJson(responseString, CBCandles.class);

          log.info("...retrieved {} candles", candleList.getCandles().size());
          return candleList.getCandles();
     }

     private String executeGetRequest(final String baseUrl, final String requestPath,
             final String requestParams, final String body) {
          OkHttpClient client = new OkHttpClient().newBuilder().build();

          String method = "GET";
          String timestamp = new Date().getTime() / 1000L + "";
          String signature = getSignature(timestamp, method, requestPath, body);

          Request request = new Request.Builder().url(
                          baseUrl + requestPath + (requestParams != null ? requestParams : ""))
                  .addHeader("Content-Type", "application/json; charset=utf-8")
                  .addHeader(CB_ACCESS_KEY, coinbaseApiKey)
                  .addHeader(CB_ACCESS_SECRET, coinbaseApiPassPhrase)
                  .addHeader(CB_ACCESS_TIMESTAMP, timestamp)
                  .addHeader(CB_ACCESS_SIGN, signature).build();

          ResponseBody responseBody = null;
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
          return responseString;
     }

     private String getSignature(final String timeStamp, final String method,
             final String path, final String body) {

          String message = timeStamp + method + path + ((body == null) ? "" : body);
          byte[] secretKey = coinbaseApiPassPhrase.getBytes(StandardCharsets.UTF_8);

          String hmacSha256 = null;
          try {
               Mac mac = Mac.getInstance(HMAC_SHA_256);
               SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, HMAC_SHA_256);
               mac.init(secretKeySpec);
               hmacSha256 = Hex.encodeHexString(
                       mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));

          } catch (Exception e) {
               throw new RuntimeException("Failed to calculate hmac-sha256", e);
          }
          return hmacSha256;
     }

}
