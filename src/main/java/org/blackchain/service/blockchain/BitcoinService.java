package org.blackchain.service.blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.transaction.BtcTransaction;
import org.blackchain.model.transaction.Transaction;
import org.blackchain.service.BlockchainComService;
import org.blackchain.util.EthereumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BitcoinService {

     @Autowired
     BlockchainComService blockchainComService;

     public Map<String, List<Transaction>> getTransactionsByToken(final String address) {
          log.info("Retrieving all BTC transactions for address: {}", address);

          final Map<String, List<Transaction>> result = new HashMap<>();
          final List<Transaction> resultList = new ArrayList<>(
                  blockchainComService.getBitcoinAddress(address).getTxs().stream()
                          .map(BtcTransaction::convertToBtcTransaction).toList());

          result.put(EthereumUtils.BITCOIN_SYMBOL, resultList);
          log.info("");

          return result;
     }

}
