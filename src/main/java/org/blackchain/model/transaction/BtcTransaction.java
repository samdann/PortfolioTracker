package org.blackchain.model.transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.blackchain.model.blockchain.com.BlockchainTx;
import org.blackchain.model.blockchain.com.TxOutput;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class BtcTransaction extends Transaction {

     private List<String> from;
     private BigDecimal fee;

     public static BtcTransaction convertToBtcTransaction(final BlockchainTx tx,
             final String address) {

          // @formatter:off
          BtcTransaction btcTransaction = BtcTransaction.builder()
                  .txHash(tx.getTxHash())
                  .blockNumber(tx.getBlockNumber())
                  .timestamp(tx.getTime())
                  .from(tx.getInputs().stream()
                          .map(input -> input.getPreviousOutput().getAddress())
                          .collect(Collectors.toList()))
                  .value(BigDecimal.valueOf(tx.getValue().longValue()))
                  .fee(tx.getFee())
                  .build();
          // @formatter:on

          List<TxOutput> filteredList = tx.getOutputs().stream()
                  .filter(out -> address.equalsIgnoreCase(out.getAddress())).toList();
          btcTransaction.setTo(
                  filteredList.size() == 1 ? filteredList.get(0).getAddress() : "N/A");

          return btcTransaction;
     }
}
