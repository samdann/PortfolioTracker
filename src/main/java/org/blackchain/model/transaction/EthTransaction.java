package org.blackchain.model.transaction;

import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
import java.math.BigInteger;
import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.blackchain.util.EthereumUtils;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class EthTransaction extends Transaction {

     private String from;
     private String tokenName;
     private String tokenSymbol;
     private BigInteger gas;
     private BigInteger gasUsed;

     public static EthTransaction convertToEthTransaction(final Tx tx) {
          return EthTransaction.builder().txHash(tx.getHash())
                  .blockNumber(BigInteger.valueOf(tx.getBlockNumber()))
                  .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                  .from(tx.getFrom()).to(tx.getTo())
                  .value(Convert.fromWei(tx.getValue().toString(), Unit.ETHER))
                  .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei()).build();

     }

     public static EthTransaction convertToEthTransaction(final TxInternal tx) {
          return EthTransaction.builder().txHash(tx.getHash())
                  .blockNumber(BigInteger.valueOf(tx.getBlockNumber()))
                  .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                  .from(tx.getFrom()).to(tx.getTo())
                  .value(Convert.fromWei(tx.getValue().toString(), Unit.ETHER))
                  .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei()).build();

     }

     public static EthTransaction convertToEthTransaction(final TxErc20 tx) {
          return EthTransaction.builder().txHash(tx.getHash())
                  .blockNumber(BigInteger.valueOf(tx.getBlockNumber()))
                  .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                  .from(tx.getFrom()).to(tx.getTo())
                  .value(EthereumUtils.convertWithTokenDecimal(tx.getValue().toString(),
                          tx.getTokenDecimal())).gas(tx.getGas().asWei())
                  .gasUsed(tx.getGasUsed().asWei()).tokenSymbol(tx.getTokenSymbol())
                  .tokenName(tx.getTokenName()).build();
     }

}
