package org.blackchain.util;

import java.time.Instant;
import java.util.regex.Pattern;
import org.blackchain.exception.AccountException;

public class BasicUtils {

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("0x[a-zA-Z0-9]{40}");

    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    public static void validateAddress(String address) {
        if (isNotAddress(address)) {
            throw new AccountException(
                    "Address [" + address + "] is not Ethereum based.");
        }
    }

    public static boolean isNotAddress(String value) {
        return isEmpty(value) || !ADDRESS_PATTERN.matcher(value).matches();
    }

    public static String instantToStringEpoch(final Instant time) {
        return String.valueOf(time.toEpochMilli() / 1000);
    }

}
