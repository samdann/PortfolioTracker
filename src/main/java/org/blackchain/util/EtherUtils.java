package org.blackchain.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class EtherUtils {

    private static final BigDecimal WEI_POW = BigDecimal.TEN.pow(18);
    
    public static BigDecimal asEther(BigInteger wei) {
        return new BigDecimal(wei).divide(WEI_POW, RoundingMode.HALF_UP);
    }
}
