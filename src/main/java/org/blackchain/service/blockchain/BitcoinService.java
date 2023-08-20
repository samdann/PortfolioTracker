package org.blackchain.service.blockchain;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.blockchain.com.BlockchainAddress;
import org.blackchain.model.transaction.Transaction;
import org.blackchain.service.BlockchainComService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BitcoinService {

     @Autowired
     BlockchainComService blockchainComService;

     public Map<String, List<Transaction>> getTransactionsByToken(final String address) {
          log.info("Retrieving all BTC transactions for address: {}", address);

          BlockchainAddress bitcoinAddress = blockchainComService.getBitcoinAddress(
                  address);
          bitcoinAddress.getTxs();

          return null;
     }

}
