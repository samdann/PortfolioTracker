package org.blackchain.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ResponseBody;
import org.blackchain.api.http.CoinbaseRestService;
import org.blackchain.model.coinbase.CoinbaseProduct;
import org.blackchain.model.coinbase.CoinbaseProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductService {

    private static final String COINBASE_BASE_URL = "https://api.coinbase.com";
    private static final String COINBASE_PATH_PRODUCTS = "/api/v3/brokerage/products";
    private static final String COINBASE_PATH_PRODUCT_CANDLES = "/api/v3/brokerage/products/{product_id}/candles";

    @Autowired
    CoinbaseRestService coinbaseRestService;

    public List<CoinbaseProduct> getCoinbaseProducts() {

        String responseString = executeGetRequest(COINBASE_BASE_URL, COINBASE_PATH_PRODUCTS);

        Gson gson = new Gson();
        CoinbaseProducts coinbaseProducts = gson.fromJson(responseString, CoinbaseProducts.class);
        return coinbaseProducts.getProducts().stream()
                .filter(product -> product.getBase_display_symbol().equalsIgnoreCase("BTC"))
                .toList();
    }

    public String getProductHistoricData(final String productId) {
        ResponseBody responseBody = coinbaseRestService.executeGetRequest(COINBASE_BASE_URL,
                COINBASE_PATH_PRODUCT_CANDLES, null);

        return null;
    }

    private String executeGetRequest(final String baseUrl, final String requestPath) {
        ResponseBody responseBody = coinbaseRestService.executeGetRequest(baseUrl,
                requestPath, null);
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
