package org.blackchain.model.transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class Transaction {

     private String txHash;
     private BigInteger blockNumber;
     private long timestamp;
     private String to;
     private BigDecimal value;
}
