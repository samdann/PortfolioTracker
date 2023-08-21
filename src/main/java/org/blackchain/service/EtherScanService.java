package org.blackchain.service;

import io.goodforgod.api.etherscan.error.EtherScanException;
import io.goodforgod.api.etherscan.error.EtherScanRateLimitException;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EtherScanService {

     private static final long RETRY_TIME = 5000;

     @Autowired
     ApiKeyService apiKeyService;

     public List<Tx> getAccountTransactions(final String address) {
          log.info("Retrieving normal transactions for address {}", address);
          final List<Tx> txs = new ArrayList<>();
          try {
               txs.addAll(apiKeyService.getEtherScanAPI().account().txs(address));
          } catch (EtherScanException ex) {
               if (ex instanceof EtherScanRateLimitException) {
                    log.warn("API Rate limit reached. Trying again in 5 seconds.");
                    new Timer().schedule(new TimerTask() {
                         @Override
                         public void run() {
                              txs.addAll(getAccountTransactions(address));
                         }
                    }, RETRY_TIME);
               }
          }

          log.info("Found {} transactions for address {}", txs.size(), address);
          return txs;
     }

     public List<TxInternal> getAccountInternalTransactions(final String address) {
          log.info("Retrieving internal transactions for address {}", address);
          final List<TxInternal> txs = new ArrayList<>();
          try {
               txs.addAll(apiKeyService.getEtherScanAPI().account().txsInternal(address));
          } catch (EtherScanException ex) {
               if (ex instanceof EtherScanRateLimitException) {
                    log.warn("API Rate limit reached. Trying again in 5 seconds.");
                    new Timer().schedule(new TimerTask() {
                         @Override
                         public void run() {
                              txs.addAll(getAccountInternalTransactions(address));
                         }
                    }, RETRY_TIME);
               }
          }

          log.info("Found {} internal transactions for address {}", txs.size(), address);
          return txs;
     }

     public List<TxErc20> getERC20Transactions(final String address) {
          log.info("Retrieving ERC20 transactions for address {}", address);
          final List<TxErc20> txErc20s = new ArrayList<>();
          try {
               txErc20s.addAll(
                       apiKeyService.getEtherScanAPI().account().txsErc20(address));
          } catch (EtherScanException ex) {
               if (ex instanceof EtherScanRateLimitException) {
                    log.warn("API Rate limit reached. Trying again in 5 seconds.");
                    new Timer().schedule(new TimerTask() {
                         @Override
                         public void run() {
                              txErc20s.addAll(getERC20Transactions(address));
                         }
                    }, RETRY_TIME);
               }
          }

          log.info("Found {} ERC20 transactions for address {}", txErc20s.size(),
                  address);

          return txErc20s;
     }

}
