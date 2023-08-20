package org.blackchain.service.blockchain;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.transaction.EthTransaction;
import org.blackchain.model.transaction.Transaction;
import org.blackchain.service.EtherScanService;
import org.blackchain.util.EthereumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

@Slf4j
@Service
public class EthereumService {

     @Autowired
     EtherScanService etherScanService;

     /**
      * Groups all transactions of a given address by token name.
      *
      * @param api     EtherScan API
      * @param address Wallet address on Ethereum
      * @return Map containing all transactions by token
      */
     public Map<String, List<Transaction>> getTransactionsByToken(final EtherScanAPI api,
             final String address) {
          log.info("Retrieving all ETH transactions for address: {}", address);

          final List<EthTransaction> transactionList = new ArrayList<>();
          // 1 - normal transactions
          List<Tx> txs = etherScanService.getAccountTransactions(api, address);
          txs.forEach(tx -> {
               if (!tx.haveError()) {

                    EthTransaction transaction = EthTransaction.builder()
                            .txHash(tx.getHash()).blockNumber(tx.getBlockNumber())
                            .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                            .from(tx.getFrom()).to(tx.getTo())
                            .value(Convert.fromWei(tx.getValue().toString(), Unit.ETHER))
                            .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                            .tokenName(EthereumUtils.ETHEREUM_NAME)
                            .tokenSymbol(EthereumUtils.ETHEREUM_SYMBOL).build();
                    transactionList.add(transaction);
               }
          });

          // 2 - internal transactions
          List<TxInternal> internalTxs = etherScanService.getAccountInternalTransactions(
                  api, address);
          internalTxs.forEach(tx -> {
               if (!tx.haveError()) {
                    EthTransaction transaction = EthTransaction.builder()
                            .txHash(tx.getHash()).blockNumber(tx.getBlockNumber())
                            .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                            .from(tx.getFrom()).to(tx.getTo())
                            .value(Convert.fromWei(tx.getValue().toString(), Unit.ETHER))
                            .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                            .tokenName(EthereumUtils.ETHEREUM_NAME)
                            .tokenSymbol(EthereumUtils.ETHEREUM_SYMBOL).build();
                    transactionList.add(transaction);
               }
          });

          // 3 - ERC20 transactions
          List<TxErc20> erc20Txs = etherScanService.getERC20Transactions(api, address);
          erc20Txs.forEach(tx -> {
               EthTransaction transaction = EthTransaction.builder().txHash(tx.getHash())
                       .blockNumber(tx.getBlockNumber())
                       .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                       .from(tx.getFrom()).to(tx.getTo())
                       .value(EthereumUtils.convertWithTokenDecimal(
                               tx.getValue().toString(), tx.getTokenDecimal()))
                       .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                       .tokenSymbol(tx.getTokenSymbol()).tokenName(tx.getTokenName())
                       .build();
               transactionList.add(transaction);
          });

          final Map<String, List<Transaction>> transactionsByToken = new HashMap<>();
          transactionList.forEach(tx -> {
               if (EthereumUtils.SUPPORTED_TOKENS.contains(tx.getTokenSymbol())) {
                    transactionsByToken.putIfAbsent(tx.getTokenSymbol(),
                            new ArrayList<>());
                    transactionsByToken.get(tx.getTokenSymbol()).add(tx);
               }

          });

          return transactionsByToken;
     }

}
