package org.blackchain.service;

import static org.blackchain.util.BasicUtils.instantToStringEpoch;

import io.goodforgod.api.etherscan.EtherScanAPI;
import io.goodforgod.api.etherscan.model.Tx;
import io.goodforgod.api.etherscan.model.TxErc20;
import io.goodforgod.api.etherscan.model.TxInternal;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.blackchain.model.Transaction;
import org.blackchain.model.coinbase.Granularity;
import org.blackchain.model.coinbase.candle.CBCandle;
import org.blackchain.model.coinbase.product.CBProduct;
import org.blackchain.model.etherscan.HistoricBalance;
import org.blackchain.model.portfolio.PairPerformance;
import org.blackchain.util.EtherUtils;
import org.blackchain.util.TransactionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

@Slf4j
@Service
public class TransactionService {

     private static final String TOKEN_ADDRESS = "";
     private static final String ETH_USD_PAIR = "ETH-USD";

     @Autowired
     EtherScanService etherScanService;

     @Autowired
     ProductService productService;

     /**
      * Retrieving a list of historic data over a default period of 300 days
      *
      * @param api     EtherScan API
      * @param address Wallet address on Ethereum
      * @return List of PairPerformance object containing all historic data
      */
     public List<PairPerformance> getHistoricPerformanceByProduct(final EtherScanAPI api,
             final String address) {

          log.info("Retrieving historic performance for address: {}", address);

          final Map<String, List<HistoricBalance>> historicBalanceMap = getHistoricBalanceMap(
                  api, address);

          historicBalanceMap.forEach((key, value) -> {

          });
          List<HistoricBalance> balanceList = null;

          List<CBCandle> productHistoricData = getProductCandles(null).stream()
                  .sorted(Comparator.comparingLong(CBCandle::getTimeStamp)).toList();

          Optional<Long> minOptional = productHistoricData.stream()
                  .map(CBCandle::getTimeStamp).min(Comparator.comparingLong(x -> x));
          Long minTime = minOptional.orElse(null);

          //build map for balances
          Map<Long, BigInteger> balanceMap = balanceList.stream().collect(
                  Collectors.toMap(HistoricBalance::getTimeStamp,
                          HistoricBalance::getBalance));

          List<Long> filteredList = new ArrayList<>(
                  balanceList.stream().map(HistoricBalance::getTimeStamp)
                          .filter(timeStamp -> timeStamp > minTime).toList());

          final List<PairPerformance> result = new ArrayList<>();
          productHistoricData.forEach(data ->

          {

               long initialPoint = !filteredList.isEmpty() ? filteredList.get(0) : 0;
               if (data.getTimeStamp() > initialPoint && filteredList.size() > 1) {
                    filteredList.remove(0);
                    initialPoint = filteredList.get(0);
               }

               BigInteger amount = balanceMap.get(initialPoint);
               BigDecimal amountInEth = Convert.fromWei(String.valueOf(amount),
                       Unit.ETHER);

               BigDecimal price = data.getClose();
               PairPerformance pair = PairPerformance.builder().time(data.getTimeStamp())
                       .amountInEth(amountInEth).price(price).token(ETH_USD_PAIR)
                       .marketValue(price.multiply(amountInEth)).build();
               result.add(pair);

          });
          return result;
     }

     /*
      * Builds a balance for a given token based on the in / out transactions value.
      */
     private Map<String, List<HistoricBalance>> getHistoricBalanceMap(
             final EtherScanAPI api, final String address) {

          final Map<String, List<HistoricBalance>> result = new HashMap<>();

          final Map<String, List<Transaction>> transactionsByToken = getTransactionsByToken(
                  api, address);
          transactionsByToken.forEach((key, value) -> {
               final List<HistoricBalance> balanceList = new ArrayList<>();
               List<Transaction> sortedList = value.stream()
                       .sorted(Comparator.comparingLong(Transaction::getTimestamp))
                       .toList();

               BigInteger balance = BigInteger.valueOf(0);
               for (Transaction tx : sortedList) {
                    balance = address.equals(tx.getTo()) ? balance.add(tx.getValue())
                            : balance.subtract(tx.getValue());
                    HistoricBalance historicBalance = HistoricBalance.builder()
                            .balance(balance).timeStamp(tx.getTimestamp()).build();
                    balanceList.add(historicBalance);
                    log.info("Balance on {} is: {} - {}.", LocalDateTime.ofInstant(
                                    Instant.ofEpochMilli(historicBalance.getTimeStamp()),
                                    TimeZone.getDefault().toZoneId()),
                            EtherUtils.asEther(historicBalance.getBalance()),
                            tx.getTokenSymbol());
               }

               result.putIfAbsent(key, balanceList);

          });

          return result;
     }

     /**
      * Groups all transactions of a given address by token name.
      *
      * @param api     EtherScan API
      * @param address Wallet address on Ethereum
      * @return Map containing all transactions by token
      */
     private Map<String, List<Transaction>> getTransactionsByToken(final EtherScanAPI api,
             final String address) {
          log.info("Retrieving all ETH transactions for address: {}", address);

          final List<Transaction> transactionList = new ArrayList<>();
          // 1 - normal transactions
          List<Tx> txs = etherScanService.getAccountTransactions(api, address);
          txs.forEach(tx -> {
               Transaction transaction = Transaction.builder().txHash(tx.getHash())
                       .blockNumber(tx.getBlockNumber())
                       .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                       .from(tx.getFrom()).to(tx.getTo()).value(tx.getValue())
                       .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                       .tokenName(TransactionUtils.ETHEREUM_NAME)
                       .tokenSymbol(TransactionUtils.ETHEREUM_SYMBOL).build();
               transactionList.add(transaction);
          });

          // 2 - internal transactions
          List<TxInternal> internalTxs = etherScanService.getAccountInternalTransactions(
                  api, address);
          internalTxs.forEach(tx -> {
               Transaction transaction = Transaction.builder().txHash(tx.getHash())
                       .blockNumber(tx.getBlockNumber())
                       .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                       .from(tx.getFrom()).to(tx.getTo()).value(tx.getValue())
                       .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                       .tokenName(TransactionUtils.ETHEREUM_NAME)
                       .tokenSymbol(TransactionUtils.ETHEREUM_SYMBOL).build();
               transactionList.add(transaction);
          });

          // 3 - ERC20 transactions
          List<TxErc20> erc20Txs = etherScanService.getERC20Transactions(api, address);
          erc20Txs.forEach(tx -> {
               Transaction transaction = Transaction.builder().txHash(tx.getHash())
                       .blockNumber(tx.getBlockNumber())
                       .timestamp(Timestamp.valueOf(tx.getTimeStamp()).getTime())
                       .from(tx.getFrom()).to(tx.getTo()).value(tx.getValue())
                       .gas(tx.getGas().asWei()).gasUsed(tx.getGasUsed().asWei())
                       .tokenSymbol(tx.getTokenSymbol()).tokenName(tx.getTokenName())
                       .build();
               transactionList.add(transaction);
          });

          final Map<String, List<Transaction>> transactionsByToken = new HashMap<>();
          transactionList.forEach(tx -> {
               transactionsByToken.putIfAbsent(tx.getTokenSymbol(), new ArrayList<>());
               transactionsByToken.get(tx.getTokenSymbol()).add(tx);
          });

          return transactionsByToken;
     }

     private List<CBCandle> getProductCandles(final String productId) {

          List<CBProduct> coinbaseProducts = productService.getCoinbaseProducts(
                  productId);

          // initializing the start & end
          Instant now = Instant.now();
          String end = instantToStringEpoch(now);
          String start = instantToStringEpoch(now.minus(300, ChronoUnit.DAYS));

          // building the queryParams
          Map<String, String> queryParams = new LinkedHashMap<>();
          queryParams.put("start", start);
          queryParams.put("end", end);
          queryParams.put("granularity", Granularity.ONE_DAY.toString());

          return productService.getProductHistoricData(productId, queryParams);

     }
}
