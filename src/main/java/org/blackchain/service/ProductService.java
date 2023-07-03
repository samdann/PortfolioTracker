package org.blackchain.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.blackchain.api.http.CoinbaseRestService;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.coinbase.candle.CBCandles;
import org.blackchain.model.coinbase.product.CBProduct;
import org.blackchain.model.coinbase.product.CBProducts;
import org.blackchain.util.EthereumUtils;
import org.blackchain.util.UrlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ProductService {

     private static final String COINBASE_BASE_URL = "https://api.coinbase.com";
     private static final String COINBASE_PATH_PRODUCTS = "/api/v3/brokerage/products";
     private static final String COINBASE_PATH_PRODUCT_CANDLES = "/api/v3/brokerage/products/{product_id}/candles";

     @Autowired
     CoinbaseRestService coinbaseRestService;

     public List<CBProduct> getCoinbaseProducts(final String ticker) {
          log.info("Reading all products" + (StringUtils.hasLength(ticker)
                  ? " with a ticker: {}" : ""), ticker);
          final List<CBProduct> result = new ArrayList<>();

          String responseString = executeGetRequest(COINBASE_BASE_URL,
                  COINBASE_PATH_PRODUCTS, null);

          Gson gson = new Gson();
          CBProducts productList = gson.fromJson(responseString, CBProducts.class);
          if (StringUtils.hasLength(ticker)) {
               result.addAll(productList.getProducts().stream().filter(product ->
                       product.getBase_display_symbol().equalsIgnoreCase(ticker)
                               && product.getProduct_id()
                               .endsWith(EthereumUtils.SUPPORTED_CURRENCIES)).toList());
          }
          return result;
     }

     public List<CBCandle> getProductHistoricData(final String productId,
             final Map<String, String> queryParams) {
          final String requestPath = COINBASE_PATH_PRODUCT_CANDLES.replace("{product_id}",
                  productId);
          String requestParams = UrlUtils.addQueryParams(queryParams);
          String responseString = executeGetRequest(COINBASE_BASE_URL, requestPath,
                  requestParams);

          Gson gson = new Gson();
          CBCandles candleList = gson.fromJson(responseString, CBCandles.class);

          return candleList.getCandles();
     }

     private String executeGetRequest(final String baseUrl, final String requestPath,
             final String requestParams) {
          ResponseBody responseBody = coinbaseRestService.executeGetRequest(baseUrl,
                  requestPath, requestParams, null);
          String responseString = null;
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
