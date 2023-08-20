package org.blackchain.model.transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.blackchain.model.blockchain.com.BlockchainTx;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BtcTransaction extends Transaction {

     private List<String> from;
     private BigInteger fee;

     public static BtcTransaction convertToBtcTransaction(final BlockchainTx tx) {

          // @formatter:off
          return BtcTransaction.builder()
                  .txHash(tx.getTxHash())
                  .blockNumber(tx.getBlockNumber())
                  .timestamp(tx.getTime())
                  .from(tx.getInputs().stream()
                          .map(input -> input.getPreviousOutput().getAddress())
                          .collect(Collectors.toList()))
                  .to(tx.getOutputs().size() == 1 ? tx.getOutputs().get(0).getAddress()
                          : "N/A")
                  .value(BigDecimal.valueOf(tx.getValue().longValue()))
                  .fee(tx.getFee())
                  .build();
          // @formatter:on
     }
}
