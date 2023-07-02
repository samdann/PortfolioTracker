package org.blackchain.model.portfolio;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PairPerformance {

    private long time;
    private BigInteger amount;
    private BigDecimal price;
    private String token;
    private BigDecimal marketValue;
}
