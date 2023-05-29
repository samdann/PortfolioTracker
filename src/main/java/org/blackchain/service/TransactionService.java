package org.blackchain.service;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.error.EtherScanException;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxErc721;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class TransactionService {

    public List<Tx> getTransactionsByAddress(EtherScanAPI api, String address) {
        log.info("...retrieving all transactions for address: {}", address);
        List<Tx> list = new ArrayList<>();
        try {
            List<Tx> txs = api.account().txs(address);
            List<TxErc20> txErc20s = api.account().txsErc20(address);
            List<TxErc721> txErc721s = api.account().txsErc721(address);
            //api.account().

            if (!CollectionUtils.isEmpty(txs)) {
                int size = txs.size();
                txs.forEach(tx -> {
                    log.info(tx.toString());
                    list.add(tx);
                });

            }
        } catch (EtherScanException ex) {
            log.error("...Error retrieving transactions: {}", ex.getMessage());
        }

        return list;
    }

}
