package org.blackchain.model.transaction;

import java.math.BigInteger;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EthTransaction extends Transaction {

     private String tokenName;
     private String tokenSymbol;
     private BigInteger gas;
     private BigInteger gasUsed;

}
