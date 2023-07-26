package org.blackchain.model;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public abstract class Transaction {

     protected String txHash;
     protected long blockNumber;
     protected long timestamp;
     protected String from;
     protected String to;
     protected BigDecimal value;
}
