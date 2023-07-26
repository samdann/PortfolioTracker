package org.blackchain.model.blockchain.com;

import java.math.BigInteger;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockchainTx {

     private BigInteger fee;
     private long time;
     private BigInteger result;
     private BigInteger balance;

}
