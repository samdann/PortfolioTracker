package org.blackchain.service;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.error.EtherScanException;
import io.goodforgod.api.etherscan.model.Abi;
import io.goodforgod.api.etherscan.model.Balance;
import io.goodforgod.api.etherscan.model.TokenBalance;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
import io.goodforgod.api.etherscan.model.Wei;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.AddressAssets;
import org.blackchain.model.Token;
import org.blackchain.model.Transaction;
import org.blackchain.model.etherscan.HistoricBalance;
import org.blackchain.util.EtherUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class TransactionService {

    private static final String TOKEN_ADDRESS = "";

    @Autowired
    EtherScanService etherScanService;

    public List<HistoricBalance> getETHTransactionsByAddress(final EtherScanAPI api,
            final String address) {
        log.info("Retrieving all ETH transactions for address: {}", address);

        final List<Transaction> transactionList = new ArrayList<>();
        // normal transactions
        List<Tx> txs = etherScanService.getAccountTransactions(api, address);
        txs.forEach(tx -> {
            Transaction transaction = Transaction.builder()
                    .txHash(tx.getHash())
                    .blockNumber(tx.getBlockNumber())
                    .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                    .from(tx.getFrom())
                    .to(tx.getTo())
                    .value(tx.getValue())
                    .gas(tx.getGas().asWei())
                    .gasUsed(tx.getGasUsed().asWei())
                    .build();
            transactionList.add(transaction);
        });

        // internal transactions
        List<TxInternal> internalTxs = etherScanService.getAccountInternalTransactions(
                api, address);
        internalTxs.forEach(tx -> {
            Transaction transaction = Transaction.builder()
                    .txHash(tx.getHash())
                    .blockNumber(tx.getBlockNumber())
                    .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                    .from(tx.getFrom())
                    .to(tx.getTo())
                    .value(tx.getValue())
                    .gas(tx.getGas().asWei())
                    .gasUsed(tx.getGasUsed().asWei())
                    .build();
            transactionList.add(transaction);
        });

        final List<HistoricBalance> balanceList = new ArrayList<>();
        List<Transaction> sortedList = transactionList.stream().sorted(
                        Comparator.comparingLong(Transaction::getTimestamp))
                .toList();

        BigInteger balance = BigInteger.valueOf(0);
        for (Transaction tx : sortedList) {
            balance = address.equals(tx.getTo()) ? balance.add(tx.getValue())
                    : balance.subtract(tx.getValue());
            HistoricBalance historicBalance = HistoricBalance.builder().balance(balance)
                    .timeStamp(tx.getTimestamp()).build();
            balanceList.add(historicBalance);
            log.info("Balance on {} is: {} ETH.",
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(historicBalance.getTimeStamp()),
                            TimeZone.getDefault().toZoneId()),
                    EtherUtils.asEther(historicBalance.getBalance()));

        }

        return balanceList;

    }

    public AddressAssets getAddressAssets(final EtherScanAPI api, String address) {

        log.info("...retrieving all assets for address: {}", address);
        AddressAssets addressAssets = AddressAssets.builder().build();
        try {

            // 1 - Eth Balance
            BigInteger ethBalance = api.account().balance(address).getBalanceInWei().asWei();
            addressAssets.setEthBalance(ethBalance);

            // 2 - transactions
            List<Tx> txs = api.account().txs(address);
            txs.forEach(tx -> {
                Transaction transaction = Transaction.builder()
                        .txHash(tx.getHash())
                        .blockNumber(tx.getBlockNumber())
                        .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                        .from(tx.getFrom())
                        .to(tx.getTo())
                        .value(tx.getValue())
                        .gas(tx.getGas().asWei())
                        .gasUsed(tx.getGasUsed().asWei())
                        .build();
                addressAssets.getTransactions().add(transaction);
            });

            // 3, ERC20 transactions/tokens
            List<TxErc20> txErc20s = api.account().txsErc20(address);
            if (!CollectionUtils.isEmpty(txErc20s)) {
                List<String> erc20Tokens = getErc20TokenAddresses(txErc20s);
                erc20Tokens.forEach(tokenAddress -> {
                    List<TxErc20> erc20s = api.account().txsErc20(address, tokenAddress);
                    TokenBalance tokenBalance = api.account().balance(address, tokenAddress);
                    Token token = Token.builder()
                            .tokenBalance(tokenBalance.getBalanceInWei().asWei())
                            .numberTx(erc20s.size()).tokenContractAddress(tokenAddress).build();

                    addressAssets.getTokens().add(token);
                });
            }
        } catch (EtherScanException ex) {
            log.error("...Error retrieving transactions: {}", ex.getMessage());
        }
        log.info("...successfully retrieved assets for address: {}", address);
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
