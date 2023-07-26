package org.blackchain.model.transaction;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;
import org.blackchain.model.Transaction;

@Data
@Builder
public class EthTransaction extends Transaction {

     private String tokenName;
     private String tokenSymbol;
     private BigInteger gas;
     private BigInteger gasUsed;

}
