package org.blackchain.util;

import com.google.gson.Gson;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.codec.binary.Hex;
import org.blackchain.model.coinbase.CoinbaseProduct;
import org.blackchain.model.coinbase.CoinbaseProducts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Slf4j
@PropertySource("secret.properties")
@Service
public class Requests {

    private static final String CB_ACCESS_KEY = "cb-access-key";
    private static final String CB_ACCESS_SECRET = "cb-access-passphrase";
    private static final String CB_ACCESS_TIMESTAMP = "cb-access-timestamp";
    private static final String CB_ACCESS_SIGN = "cb-access-sign";
    private static final String HMAC_SHA_256 = "HmacSHA256";


    @Value("${coinbase.api.key}")
    private String coinbaseApiKey;

    @Value("${coinbase.api.passphrase}")
    private String coinbaseApiPassPhrase;

    private static String calcHmacSha256(byte[] secretKey, String message) {
        String hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA_256);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, HMAC_SHA_256);
            mac.init(secretKeySpec);
            hmacSha256 = Hex.encodeHexString(mac.doFinal(message.getBytes(StandardCharsets.UTF_8)));


        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    public List<CoinbaseProduct> getRequest() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String method = "GET";
        String body = null;
        String requestPath = "/api/v3/brokerage/products";
        String timestamp = new Date().getTime() / 1000L + "";
        String signature = getSignature(timestamp, method, requestPath, body);

        Request request = new Request.Builder()
                .url("https://coinbase.com" + requestPath)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader(CB_ACCESS_KEY, coinbaseApiKey)
                .addHeader(CB_ACCESS_SECRET, coinbaseApiPassPhrase)
                .addHeader(CB_ACCESS_TIMESTAMP, timestamp)
                .addHeader(CB_ACCESS_SIGN, signature)
                .addHeader("CB-VERSION", "2023-06-22")
                .build();
        ResponseBody responseBody = client.newCall(request).execute().body();
        assert responseBody != null;
        return convertToJson(responseBody.string());


    }

    private String getSignature(String timeStamp, String method, String path, String body) {

        String message = timeStamp + method + path + ((body == null) ? "" : body);
        log.info(message);
        return calcHmacSha256(coinbaseApiPassPhrase.getBytes(StandardCharsets.UTF_8), message);

    }

    private List<CoinbaseProduct> convertToJson(String responseString) {
        log.info(responseString);
        Gson gson = new Gson();

        CoinbaseProducts coinbaseProducts = gson.fromJson(responseString, CoinbaseProducts.class);

        return coinbaseProducts.getProducts().stream()
                .filter(product -> product.getBase_display_symbol().equalsIgnoreCase("BTC"))
                .toList();

    }


}
