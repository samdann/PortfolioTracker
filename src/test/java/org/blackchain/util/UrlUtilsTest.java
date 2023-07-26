package org.blackchain.util;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UrlUtilsTest {

    @Test
    public void canAddQueryParams() {
        final Map<String, String> queryParams = new HashMap<>();

        String url = UrlUtils.addQueryParams(queryParams);
        Assertions.assertFalse(url.contains("?"));

        queryParams.put("start", "123456789");
        String url1 = UrlUtils.addQueryParams(queryParams);
        Assertions.assertTrue(url1.contains("?"));
        Assertions.assertFalse(url1.contains("&"));

        queryParams.put("end", "123466789");
        String url2 = UrlUtils.addQueryParams(queryParams);
        Assertions.assertTrue(url2.contains("&"));
    }

}
