package org.blackchain.service;


import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.error.EtherScanException;
import io.goodforgod.api.etherscan.error.EtherScanRateLimitException;
import io.goodforgod.api.etherscan.model.Tx;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EtherScanService {

    private static final long RETRY_TIME = 5000;

    public List<Tx> getAccountTransactions(final EtherScanAPI api, final String address) {
        log.info("Retrieving transactions for address {}", address);
        final List<Tx> txs = new ArrayList<>();
        try {
            txs.addAll(api.account().txs(address));
        } catch (EtherScanException ex) {
            if (ex instanceof EtherScanRateLimitException) {
                log.warn("API Rate limit reached. Trying again in 5 seconds.");
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                txs.addAll(getAccountTransactions(api, address));
                            }
                        }, RETRY_TIME
                );
            }
        }

        log.info("Found {} transactions for address {}", txs.size(), address);
        return txs;
    }

}