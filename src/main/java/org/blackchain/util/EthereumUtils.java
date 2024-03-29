package org.blackchain.util;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class EthereumUtils {

     // Ethereum
     public static final String ETHEREUM_NAME = "Ethereum";
     public static final String ETHEREUM_SYMBOL = "ETH";

     // Bitcoin
     public static final String BITCOIN_NAME = "Bitcoin";
     public static final String BITCOIN_SYMBOL = "BTC";

     //General
     public static final String SUPPORTED_CURRENCIES = "USD";
     public static final List<String> SUPPORTED_TOKENS = Arrays.asList("MATIC", "USDT",
             "ETH");
     private static final Pattern ADDRESS_PATTERN = Pattern.compile("0x[a-zA-Z0-9]{40}");

     public static boolean isEthereumAddress(final String address) {
          return !isEmpty(address) && ADDRESS_PATTERN.matcher(address).matches();

     }

     private static boolean isEmpty(String value) {
          return value == null || value.isEmpty();
     }

     public static BigDecimal convertWithTokenDecimal(final String value,
             final String tokenDecimal) {
          BigDecimal pow = BigDecimal.TEN.pow(Integer.parseInt(tokenDecimal));
          return new BigDecimal(value).divide(pow);
     }
}
