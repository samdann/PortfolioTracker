package org.blackchain.service;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.error.EtherScanException;
import io.goodforgod.api.etherscan.model.Abi;
import io.goodforgod.api.etherscan.model.Balance;
import io.goodforgod.api.etherscan.model.TokenBalance;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.Wei;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.AddressAssets;
import org.blackchain.model.Token;
import org.blackchain.model.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class TransactionService {

    private static final String TOKEN_ADDRESS = "";

    public AddressAssets getAddressAssets(EtherScanAPI api, String address) {
        log.info("...retrieving all assets for address: {}", address);
        AddressAssets addressAssets = new AddressAssets();
        try {

            // first, transactions
            List<Tx> txs = api.account().txs(address);
            txs.forEach(tx -> {
                Transaction transaction = Transaction.builder().from(tx.getFrom()).to(tx.getTo())
                        .value(tx.getValue()).build();
                addressAssets.getTransactions().add(transaction);
            });

            // second, ERC20 transactions/tokens
            List<TxErc20> txErc20s = api.account().txsErc20(address);
            if (!CollectionUtils.isEmpty(txErc20s)) {
                List<String> erc20Tokens = getErc20TokenAddresses(txErc20s);
                erc20Tokens.forEach(tokenAddress -> {
                    List<TxErc20> erc20s = api.account().txsErc20(address, tokenAddress);
                    TokenBalance tokenBalance = api.account().balance(address, tokenAddress);
                    Token token = new Token();
                    token.setTokenBalance(tokenBalance.getBalanceInWei().asWei());
                    token.setNumberTx(erc20s.size());
                    token.setTokenContractAddress(tokenAddress);
                    addressAssets.getTokens().add(token);
                });
            }
        } catch (EtherScanException ex) {
            log.error("...Error retrieving transactions: {}", ex.getMessage());
        }
        return addressAssets;
    }

    private List<String> getErc20TokenAddresses(final List<TxErc20> txs) {
        return txs.stream().map(tx -> tx.getContractAddress()).filter(address -> !address.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private void check(EtherScanAPI api, String address, String txHash) {

        //balance of an address
        List<Balance> balances = api.account().balances(Arrays.asList(address, address));

        //all transactions of an address (limit to 10k)
        List<Tx> txs = api.account().txs(address);

        // list of all erc20 transactions of a specific token for a given address
        List<TxErc20> txErc20s = api.account().txsErc20(address, TOKEN_ADDRESS);

        // all internal transactions : need to be checked.
        api.account().txsInternal(address);

        // search transaction by hash.
        api.account().txsInternalByHash(txHash);

        // contract abi
        Abi contractAbi = api.contract().contractAbi(TOKEN_ADDRESS);

        Duration estimate = api.gasTracker().estimate(Wei.ofWei(12));


    }

}
