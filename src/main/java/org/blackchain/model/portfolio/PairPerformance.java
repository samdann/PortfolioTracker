package org.blackchain.model.portfolio;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PairPerformance {

     private long time;
     private BigDecimal amountInEth;
     private BigDecimal amountInToken;
     private BigDecimal price;
     private String token;
     private BigDecimal marketValue;
}
