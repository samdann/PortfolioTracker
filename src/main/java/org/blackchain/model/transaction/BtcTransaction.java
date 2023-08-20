package org.blackchain.model.transaction;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.blackchain.model.blockchain.com.BlockchainTx;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BtcTransaction extends Transaction {

     private List<String> from;

     public static BtcTransaction convertToBtcTransaction(final BlockchainTx tx) {

          return BtcTransaction.builder().txHash(tx.getTxHash())
                  .blockNumber(tx.getBlockNumber()).timestamp(tx.getTime()).build();
     }
}
