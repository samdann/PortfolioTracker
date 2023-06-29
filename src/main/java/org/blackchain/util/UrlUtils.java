package org.blackchain.util;

import java.util.Map;
import java.util.stream.Collectors;

public class UrlUtils {

    private static final String EQUALS_STRING = "=";
    private static final String QUESTION_MARK_STRING = "?";

    public static String addQueryParams(final Map<String, String> queryParams) {
        StringBuffer sb = new StringBuffer();
        if (!queryParams.isEmpty()) {
            sb.append("?");
            sb.append(queryParams.entrySet().stream()
                    .map(entry -> entry.getKey() + EQUALS_STRING + entry.getValue()).collect(
                            Collectors.joining(QUESTION_MARK_STRING)));
        }
        return sb.toString();
    }

}
