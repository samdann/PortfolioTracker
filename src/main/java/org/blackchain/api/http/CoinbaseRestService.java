package org.blackchain.api.http;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Slf4j
@PropertySource("secret.properties")
@Service
public class CoinbaseRestService implements RestAPI {

    private static final String CB_ACCESS_KEY = "cb-access-key";
    private static final String CB_ACCESS_SECRET = "cb-access-passphrase";
    private static final String CB_ACCESS_TIMESTAMP = "cb-access-timestamp";
    private static final String CB_ACCESS_SIGN = "cb-access-sign";
    private static final String HMAC_SHA_256 = "HmacSHA256";


    @Value("${coinbase.api.key}")
    private String coinbaseApiKey;

    @Value("${coinbase.api.passphrase}")
    private String coinbaseApiPassPhrase;

    @Override
    public ResponseBody executeGetRequest(final String baseUrl, final String requestPath,
            final String requestParams,
            final String body) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        String method = "GET";
        String timestamp = new Date().getTime() / 1000L + "";
        String signature = getSignature(timestamp, method, requestPath, body);

        Request request = new Request.Builder()
                .url(baseUrl + requestPath + requestParams)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .addHeader(CB_ACCESS_KEY, coinbaseApiKey)
                .addHeader(CB_ACCESS_SECRET, coinbaseApiPassPhrase)
                .addHeader(CB_ACCESS_TIMESTAMP, timestamp)
                .addHeader(CB_ACCESS_SIGN, signature)
                .build();

        ResponseBody responseBody = null;
        try {
            responseBody = client.newCall(request).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assert responseBody != null;
        return responseBody;
    }
    

    private String getSignature(final String timeStamp, final String method, final String path,
            final String body) {

        String message = timeStamp + method + path + ((body == null) ? "" : body);
        byte[] secretKey = coinbaseApiPassPhrase.getBytes(StandardCharsets.UTF_8);

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


}
