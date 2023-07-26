package org.blackchain.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Transaction {

     private String txHash;
     private long blockNumber;
     private long timestamp;
     private String from;
     private String to;
     private BigDecimal value;
}
