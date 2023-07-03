package org.blackchain.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

public class EthereumUtils {

     // Ethereum
     public static final String ETHEREUM_NAME = "Ethereum";
     public static final String ETHEREUM_SYMBOL = "ETH";

     //General
     public static final String SUPPORTED_CURRENCIES = "USD";
     public static final List<String> SUPPORTED_TOKENS = Arrays.asList("MATIC", "USDT",
             "ETH");

     private static final BigDecimal WEI_POW = BigDecimal.TEN.pow(18);

     public static BigDecimal asEther(BigInteger wei) {
          return new BigDecimal(wei).divide(WEI_POW, RoundingMode.HALF_UP);
     }

     public static BigDecimal convertWithTokenDecimal(final String value,
             final String tokenDecimal) {
          BigDecimal pow = BigDecimal.TEN.pow(Integer.parseInt(tokenDecimal));
          return new BigDecimal(value).divide(pow);
     }
}
