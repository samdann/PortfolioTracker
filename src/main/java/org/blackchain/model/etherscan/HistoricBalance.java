package org.blackchain.model.etherscan;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HistoricBalance {

     private long timeStamp;
     private BigDecimal balance;
}
