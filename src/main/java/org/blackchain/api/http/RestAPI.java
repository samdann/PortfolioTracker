package org.blackchain.api.http;

import okhttp3.ResponseBody;

public interface RestAPI {

    public ResponseBody executeGetRequest(final String baseUrl, final String requestPath,
            final String requestParams,
            final String body);

}
