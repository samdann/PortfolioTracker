package org.blackchain.service.blockchain;

import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
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

@Slf4j
@Service
public class EthereumService {

     @Autowired
     EtherScanService etherScanService;

     /**
      * Groups all transactions of a given address by token name.
      *
      * @param address Wallet address on Ethereum
      * @return Map containing all transactions by token
      */
     public Map<String, List<Transaction>> getTransactionsByToken(final String address) {
          log.info("{} : retrieving all Ethereum transactions", address);

          final List<EthTransaction> transactionList = new ArrayList<>();
          // 1 - normal transactions
          List<Tx> txs = etherScanService.getAccountTransactions(address);
          txs.forEach(tx -> {
               if (!tx.haveError()) {
                    EthTransaction transaction = EthTransaction.convertToEthTransaction(
                            tx);
                    transaction.setTokenName(EthereumUtils.ETHEREUM_NAME);
                    transaction.setTokenSymbol(EthereumUtils.ETHEREUM_SYMBOL);
                    transactionList.add(transaction);
               }
          });

          // 2 - internal transactions
          List<TxInternal> internalTxs = etherScanService.getAccountInternalTransactions(
                  address);
          internalTxs.forEach(tx -> {
               if (!tx.haveError()) {
                    EthTransaction transaction = EthTransaction.convertToEthTransaction(
                            tx);
                    transaction.setTokenName(EthereumUtils.ETHEREUM_NAME);
                    transaction.setTokenSymbol(EthereumUtils.ETHEREUM_SYMBOL);
                    transactionList.add(transaction);
               }
          });

          // 3 - ERC20 transactions
          List<TxErc20> erc20Txs = etherScanService.getERC20Transactions(address);
          erc20Txs.forEach(tx -> {
               EthTransaction transaction = EthTransaction.convertToEthTransaction(tx);
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
          transactionsByToken.forEach(
                  (key, value) -> log.info("{} : found {} transactions for :{},", address,
                          value.size(), key));

          return transactionsByToken;
     }

}
