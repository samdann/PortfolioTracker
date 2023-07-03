package org.blackchain.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaction {

     private String tokenName;
     private String tokenSymbol;
     private String txHash;
     private long blockNumber;
     private long timestamp;
     private String from;
     private String to;
     private BigDecimal value;
     private BigInteger gas;
     private BigInteger gasUsed;

}
