package org.blackchain.util;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Slf4j
@PropertySource("secret.properties")
@Service
public class Requests {

    private static final String CB_ACCESS_KEY = "cb-access-key";
    private static final String CB_ACCESS_PASSPHRASE = "cb-access-passphrase";
    private static final String CB_ACCESS_TIMESTAMP = "cb-access-timestamp";
    private static final String CB_ACCESS_SIGN = "cb-access-sign";
    private static final String HMAC_SHA256_ALGO = "HmacSHA256";


    @Value("${coinbase.api.key}")
    private String coinbaseApiKey;

    @Value("${coinbase.api.passphrase}")
    private String coinbaseApiPassPhrase;

    private static byte[] calcHmacSha256(byte[] secretKey, byte[] message) {
        byte[] hmacSha256 = null;
        try {
            Mac mac = Mac.getInstance(HMAC_SHA256_ALGO);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, HMAC_SHA256_ALGO);
            mac.init(secretKeySpec);
            hmacSha256 = mac.doFinal(message);


        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate hmac-sha256", e);
        }
        return hmacSha256;
    }

    public void getRequest() throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String method = "GET";
        String timestamp = new Date().getTime() / 1000 + "";
        String signature = getSignature(timestamp, "GET", "/address-book", "");

        Request request = new Request.Builder()
                .url("https://api.exchange.coinbase.com/address-book")
                .addHeader("Content-Type", "application/json")
                .addHeader(CB_ACCESS_KEY, coinbaseApiKey)
                .addHeader(CB_ACCESS_PASSPHRASE, coinbaseApiPassPhrase)
                .addHeader(CB_ACCESS_TIMESTAMP, timestamp)
                .addHeader(CB_ACCESS_SIGN, signature)
                .addHeader("CB-VERSION", "2023-06-22")
                .addHeader("User-Agent", "request")
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        log.info(response.body().string());

    }

    private String getSignature(String timeStamp, String method, String path, String body) {

        byte[] secretKey = Base64.getDecoder().decode(coinbaseApiKey.getBytes());
        String message = timeStamp + method + path + body;

        byte[] hmacSha256 = calcHmacSha256(secretKey, message.getBytes());

        return Base64.getEncoder().encodeToString(hmacSha256);
    }


}
